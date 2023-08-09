# 基于 tcp 监控 Http 应用

> tcp 属于传输层，对应用层协议的识别有限，目前部分协议即使通过数据包来识别，但解析的协议不够准备，需要配合 uprobe（用户态应用探针）来解析具体应用协议，然而此操作只能针对具体应用才可用，不具备移植性。

## 代码

```c
#ifndef BPFTRACE_HAVE_BTF
#include <linux/socket.h>
#include <net/sock.h>
#else
#include <sys/socket.h>
#endif

BEGIN
{
	printf("Tracing TCP accepts. Hit Ctrl-C to end.\n");
	printf("%-30s ","Probe");
	printf("%-8s %-6s %-14s ", "TIME", "PID", "COMM");
	printf("%-39s %-5s %-39s %-5s %s\n", "RADDR", "RPORT", "LADDR","LPORT", "BL");
}

/**
 * TCP 生命周期，会监控所有 tcp 操作，会影响服务器性能
 */
// tracepoint:sock:inet_sock_set_state
// {
// 	$sk=(struct sock *)args->skaddr;
// 	$protocol=args->protocol;
// 	$sport=args->sport;
// 	$dport=args->dport;
// 	$saddr=ntop(args->saddr);
// 	$daddr=ntop(args->daddr);
// 	$saddr_v6=ntop(args->saddr_v6);
// 	$daddr_v6=ntop(args->daddr_v6);
	
// 	printf("%-30s ","inet_sock_set_state");
// 	time("%H:%M:%S ");
// 	printf("%-6d %-14s ", pid, comm);
// 	printf("%-39s %-5d %-39s %-5d ", $daddr, $dport, $saddr, $sport);
// 	printf("%-39s/%-39s\n", $saddr_v6, $daddr_v6);
// }

kretprobe:inet_csk_accept
{	
	$sk = (struct sock *)retval;	
	$inet_family = $sk->__sk_common.skc_family;

	if ($inet_family == AF_INET || $inet_family == AF_INET6) {
		// initialize variable type:
		$daddr = ntop(0);
		$saddr = ntop(0);
		if ($inet_family == AF_INET) {
			$daddr = ntop($sk->__sk_common.skc_daddr);
			$saddr = ntop($sk->__sk_common.skc_rcv_saddr);
		} else {
			$daddr = ntop(
			    $sk->__sk_common.skc_v6_daddr.in6_u.u6_addr8);
			$saddr = ntop(
			    $sk->__sk_common.skc_v6_rcv_saddr.in6_u.u6_addr8);
		}
		$lport = $sk->__sk_common.skc_num;
		$dport = $sk->__sk_common.skc_dport;
		$qlen  = $sk->sk_ack_backlog;
		$qmax  = $sk->sk_max_ack_backlog;

		// Destination port is big endian, it must be flipped
		// $dport = bswap($dport);

		printf("%-30s ","inet_csk_accept");
		time("%H:%M:%S ");
		printf("%-6d %-14s ", pid, comm);
		printf("%-39s %-5d %-39s %-5d ", $daddr, $dport, $saddr, $lport);
		printf("%d/%d\n", $qlen, $qmax);
	}
}

/**
 * 访问服务器未进入该探针
 */
// kretprobe:inet_accept{
// 	$sk = (struct sock *)retval;
// 	$inet_family = $sk->__sk_common.skc_family;

// 	if ($inet_family == AF_INET || $inet_family == AF_INET6) {
// 		// initialize variable type:
// 		$daddr = ntop(0);
// 		$saddr = ntop(0);
// 		if ($inet_family == AF_INET) {
// 			$daddr = ntop($sk->__sk_common.skc_daddr);
// 			$saddr = ntop($sk->__sk_common.skc_rcv_saddr);
// 		} else {
// 			$daddr = ntop($sk->__sk_common.skc_v6_daddr.in6_u.u6_addr8);
// 			$saddr = ntop($sk->__sk_common.skc_v6_rcv_saddr.in6_u.u6_addr8);
// 		}
// 		$lport = $sk->__sk_common.skc_num;
// 		$dport = $sk->__sk_common.skc_dport;
// 		$qlen  = $sk->sk_ack_backlog;
// 		$qmax  = $sk->sk_max_ack_backlog;

// 		// Destination port is big endian, it must be flipped
// 		// $dport = bswap($dport);

// 		printf("%-30s ","inet_accept");
// 		time("%H:%M:%S ");
// 		printf("%-6d %-14s ", pid, comm);
// 		printf("%-39s %-5d %-39s %-5d ", $daddr, $dport, $saddr, $lport);
// 		printf("%d/%d\n", $qlen, $qmax);
// 	}
// }

/*
kprobe:tcp_v4_rcv{
    $skb=(struct sk_buff *) arg0;
    // struct iphdr *iph=(struct iphdr *)($skb->head + $skb->network_header);
    // struct tcphdr *th =(struct tcphdr *)($skb->head + $skb->transport_header);
    $iph=(struct iphdr *)($skb->head + $skb->network_header);
    $th=(struct tcphdr *)($skb->head + $skb->transport_header);

    $ip_proto = $iph->protocol;    
    $saddr = $iph->saddr;
    $daddr = $iph->daddr;
    
    $sport = $th->source;
    $dport = $th->dest;
}

kprobe:ip_rcv{
    $skb=(struct sk_buff *) arg0;
}

kprobe:ip_rcv_finish{
    $skb=(struct sk_buff *) arg1;
}

kprobe:tcp_v4_send_reset{
    $skb=(struct sk_buff *) arg1;
}
*/

tracepoint:syscalls:sys_enter_accept{
    printf("%-30s ","sys_enter_accept");
    time("%H:%M:%S ");
    printf("%-6d %-14s \n", pid, comm);
}

/**
 * 基于uprobe监测应用的数据
 */
```

## FAQ

### eBPF 性能损耗

> eBPF 对系统的负载必然会产生影响，但合理的选择探针对负载的影响相对会小一点，根据资料，探针使用优先级：`tracepoint > kprobe`，`usdt > uprobe`。

### eBPF 异步

从目前查到的资料，eBPF 非异步的，或者说 eBPF 异步的实现是由具体的 BPF 内核端代码是否是异步来实现的【采自 ChatGPT】

参考资料：

1. <https://www.cnblogs.com/honpey/p/4575902.html>

### bpftrace 脚本

bpftrace 是基于 bcc 实现的工具，由此 bpftrace 相对自由度会低一点，实现的逻辑复杂度不适宜过高

### Linux 内核

eBPF 对内核态的监控，需要对 Linux 内核源码熟悉，对于 kprobe 来说，无任何工具提供具体参数，需根据源码获取参数，对需要的参数自行进行使用

脚本支持 Linux 内核对外开放的接口函数？？？

**TCP**

> 对具体协议解析困难

数据包由内核态传输到用户态，数据包大小的限制？？？

数据包分段传输情况

## 用户态应用监测

**Java 应用**

在使用 uprobe 监测应用的情况下，对 Java 应用来说找到对应探针异常麻烦，因为定位探针操作需要熟悉 Java 应用转换后对应的符号表，才能具体找到对应的探针。