//package org.luncert.mx1.probe.daemon.impl;
//
//import org.luncert.mx1.probe.daemon.StubDataReceiver;
//
//public class StubDataReceiverFactory {
//
//  public StubDataReceiver createReceiver(EStubDataReceiverType type) {
//    if (type == null) {
//      throw new NullPointerException("type must be non null");
//    }
//    if (EStubDataReceiverType.SharedMemory.equals(type)) {
//      return new ShmStubDataReceiver();
//    }
//    if (EStubDataReceiverType.Udp.equals(type)) {
//      return new UdpStubDataReceiver();
//    }
//    throw new RuntimeException("failed to create stub data receiver, invalid type provided: " + type);
//  }
//}
