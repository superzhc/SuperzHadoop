package com.github.superzhc.xxljob.controller;

import com.github.superzhc.xxljob.common.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Firmware;
import oshi.hardware.GlobalMemory;
import oshi.hardware.VirtualMemory;
import oshi.software.os.OSProcess;

import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.*;

/**
 * 使用 <a href="https://github.com/oshi/oshi">oshi-core</a> 库来获取系统信息
 * 使用示例参考：<a>https://atovk.github.io/post/java/oshi_system_and_hardware_information/</a>
 *
 * @author superz
 * @create 2021/7/28 9:30
 */
@RestController
@RequestMapping("/system/info")
public class SystemInfoController {
    private static final Logger log = LoggerFactory.getLogger(SystemInfoController.class);

    @Autowired
    private SystemInfo systemInfo;

    // 获取硬件信息
    //HardwareAbstractionLayer hardware = systemInfo.getHardware();
    // 获取操作系统进程相关信息
    //OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

    /**
     * BIOS 信息
     *
     * @return
     */
    @RequestMapping("/bios")
    public ResultT BIOS() {
        ComputerSystem computerSystem = systemInfo.getHardware().getComputerSystem();
        Firmware firmware = computerSystem.getFirmware();
        Map<String, Object> data = new HashMap<>();
        data.put("name", firmware.getName());
        data.put("description", firmware.getDescription());
        data.put("manufacturer", firmware.getManufacturer());
        data.put("releaseDate", firmware.getReleaseDate());
        data.put("version", firmware.getVersion());
        return ResultT.success(data);
    }

    /**
     * 内存信息
     *
     * @return
     */
    @RequestMapping("/memory")
    public ResultT memory() {
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        Map<String, Object> data = new HashMap<>();
        data.put("total", memory.getTotal());
        data.put("available", memory.getAvailable());
        data.put("page_size", memory.getPageSize());
        data.put("virtual_memory", memory.getVirtualMemory());
        return ResultT.success(data);
    }

    /**
     * 获取操作系统类别
     * linux/MACOS/unix/windows 等
     *
     * @return
     */
    @RequestMapping("/os")
    public ResultT os() {
        Map<String, Object> data = new HashMap<>();
        data.put("family", systemInfo.getOperatingSystem().getFamily());
        data.put("os_name", System.getProperties().getProperty("os.name"));
        data.put("os_arch", System.getProperties().getProperty("os.arch"));
        return ResultT.success(data);
    }

    @RequestMapping("/server")
    public ResultT server() {
        Map<String, Object> data = new HashMap<>();
        data.put("ip", ip());
        data.put("host", host());
        data.put("start_date", new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
        return ResultT.success(data);
    }

    private String ip() {
        try {
            // 得到本机所有的物理网络接口和虚拟机等软件利用本机的物理网络接口创建的逻辑网络接口的信息
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = networkInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // IPV4
                    if (inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        // 排除虚拟IP
                        if (!ip.endsWith(".1")) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("获取IP地址异常", e);
        }
        return "127.0.0.1";
    }

    private String host() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("获取主机名异常", e);
        }
        return "未知";
    }

    /**
     * 获取所有进程信息
     *
     * @return
     */
    @RequestMapping("/processes")
    public ResultT processes() {
        List<OSProcess> lst = systemInfo.getOperatingSystem().getProcesses();
        return ResultT.success(lst);
    }

    @RequestMapping("/jvm")
    public ResultT jvm() {
        Map<String, Object> data = new HashMap<>();
        data.put("version", System.getProperties().getProperty("java.version"));
        data.put("total", Runtime.getRuntime().totalMemory());
        data.put("max", Runtime.getRuntime().maxMemory());
        data.put("free", Runtime.getRuntime().freeMemory());
        return ResultT.success();
    }
}
