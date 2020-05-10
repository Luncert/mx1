package org.luncert.mx1.probe.daemon;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.constant.CoreAction;
import org.luncert.mx1.commons.data.DataPacket;
import org.luncert.mx1.commons.util.DataPacketUtils;

@Slf4j
public class CentralDataHandler extends ChannelInboundHandlerAdapter {
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    DataPacket packet = (DataPacket) msg;
    if (CoreAction.REP_NODE_ID.equals(packet.getAction()) &&
        DataPacketUtils.validateDataType(packet, String.class)) {
      String nodeId = (String) packet.getData();
      NodeIdHolder.set(nodeId);
      log.info("Registered probe with id: {}", nodeId);
    } else {
    
    }
    
    ctx.fireChannelRead(msg);
  }
}
