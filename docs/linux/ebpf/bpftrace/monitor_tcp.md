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
```