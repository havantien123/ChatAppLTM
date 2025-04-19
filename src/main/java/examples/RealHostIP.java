package examples;

import java.net.*;
import java.util.Enumeration;

public class RealHostIP {
    public static void main(String[] args) {
        try {
            String realIP = getRealHostIP();
            System.out.println("Real Host IP: " + realIP);
        } catch (SocketException e) {
            System.err.println("Error getting network interfaces: " + e.getMessage());
        }
    }

    public static String getRealHostIP() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            // Bỏ qua các giao diện ảo: loopback, docker, VPN, virtual machine
            if (networkInterface.isLoopback() ||
                    networkInterface.isVirtual() ||
                    !networkInterface.isUp() ||
                    networkInterface.getDisplayName().contains("docker") ||
                    networkInterface.getDisplayName().contains("VMware") ||
                    networkInterface.getDisplayName().contains("VirtualBox")) {
                continue;
            }

            // Lấy địa chỉ IP của giao diện mạng hợp lệ
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();

                // Chỉ lấy IPv4 (bỏ qua IPv6)
                if (address instanceof Inet4Address) {
                    return address.getHostAddress();
                }
            }
        }

        return "127.0.0.1"; // Fallback to loopback if no real IP found
    }
}