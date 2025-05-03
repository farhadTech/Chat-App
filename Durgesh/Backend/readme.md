# Introduction to WebSocket
WebSocket is a protocol that enables full-duplex communication between a client and server over a single, long-lived connection. Unlike HTTP, where the client must initiate every request, WebSocket allows both client and server to send data at any time, making it ideal for real-time applications like chat apps, notifications, live feeds, etc.

## Key Concepts of WebSocket
Feature                   Description:
**Full-duplex**           Server and client can send messages independently.
**Persistent Connection** The connection remains open, reducing the overhead of HTTP.
**Low Latency**           Ideal for real-time communication.


## Create a WebSocket Configuration
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Override
  public void registerStomEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws") // endpoint for client
    .setAllowdOrginPatterns("*") // allow any origin
    .withSockJS(); // fallback for browsers that don't suppor WebSocket
   }

   @Override
   public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app");
   }
}
```

## Create a Message Model
public class ChatMessage {
  private String sender;
  private String content;
  private String time;

  // getters and setters
}

## Create a Controller to Handle Messages
```java
@Controller
public class ChatController {
  @MessageMapping("/chat") // matches /app/chat
  @SendTo("/topic/messages") // sends to /topic/messages
  public ChatMessage send(ChatMessage message) {
    message.setTime(Java.time.LocalTime.now().toString());
    return message;
  }
 }
 ```

## Sample Frontend HTML with SockJS and Stomp
```html
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.0/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

<script>
  const socket = new SockJS("/ws");
  const stompClient = Stomp.over(socket);

  stompClient.connect({}, function() {
    stompClient.subscribe("/topic/messages", function(message) {
      const chat = JSON.parse(message.body);
      console.log(chat);
    });
  });

  function sendMessage() {
    const msg = {
      sender: "Farhad",
      content: "Hello!"
    };
    stompClient.send("/app/chat", {}, JSON.stringify(msg));
  }
<script>
```

## Summary
* `/ws` is the WebSocket endpoint.
* Clients send messages to `/app/chat`.
* Server broadcasts messages to `/topic/messages`.
* Uses **STOMP** over WebSocket (for a pub-sub pattern).

