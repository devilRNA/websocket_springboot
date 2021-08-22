package entities;

import com.alibaba.fastjson.JSON;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class messageEncoder implements Encoder.Text<message>{
    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    @Override
    public String encode(message msgA) throws EncodeException {
        return JSON.toJSONString(msgA);
    }

}
