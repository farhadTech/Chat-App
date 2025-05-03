# How things works?

## 1. Class Overview
```java
public class WebSocket implements WebSocketMessageBrokerConfigurer {

}
```
We are implementing WebSocketMessageBroker configurer, which allows us to customize the WebSocket/STOMP message broker configuration.

## 2. Registering STOMP Endpoints
```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
  registry.addEndpoint("/chat")
  .setAllowedOrigins("http://localhost:8080")
  .withSockJS();
}
```

STOMP: **STOMP is "the Simple (or Streaming) Text Oriented Messaging Protocol."**
It uses a set of commands like CONNECT, SEND or SUBSCRIBE to manage the conversation.
STOMP clients, written in any language, can talk with any message broker supporting the protocol.

* `/chat`
Clients will open their WebSocket (or SockJS) connection to `ws://<host>:<port>/chat`.
* `.setAllowedOrigins("http://localhost:8080")`
Enables CORS for browser-based clients served from `http://localhost:8080` in our thymeleaf.
* `.withSockJS()`
Tells Spring to fall back to SockJS when native WebSocket isn't available (older browsers, certain proxies etc.).
SockJS emulates the WebSocket API over HTTP.

## 3. Configuring the MessageBroker
```java
public void configureMessageBroker(MessageBrokerRegistry registry) {
  registry.enableSimpleBroker("/topic");
  registry.setApplicationDestinationPrefixes("/app");
}
```

* `.enableSimpleBroker("/topic")`
Activates an in-memory message broker that routes messages whose destinations start with `/topic.`

* For example, if a client subscribes to `/topic/messages`, messages sent there will be broadcast by this simple broker.

* `.setApplicationDestinationPrefixes("/app")`
Any messages sent from clients whose destination begins with /app are mapped to @MessageMapping methods in our @Controller class.

## Controller

## Class Definition
```java
@Controller
public class ChatController {

}
```
* `@Controller` marks this class as Spring MVC controller, which can handle both:
* WebSocket Messages (via @MessageMapping)
* HTTP request (via @GetMapping)

## WebSocket Handler Method
```java
@MessageMapping("/sendMessage")
@SendTo("/topic/messages")
public ChatMessage sendMessasge(ChatMessage message) {
  return message;
}
```

This method is for handling real-time WebSocket communication.

* `@MessageMapping("/sendMessage)`
  * This listens for messages sent from the client to the destination `/app/sendMessage`.
  * The `/app` prefix is defined in our WebSocket config via `.setApplicationDestinationPrefixes("/app")`
* `@SendTo("topic/messages")`
  * After processing the message, the result is broadcast to all clients subscribed to `/topic/messages`.
  * The `/topic` prefix matches what we enabled via `enableSimpleBroker("/topic")`.
* `ChatMessage message`
  * The payload sent from the client is mapped to a java object (ChatMessage).
  * This object is returned and then sent to all subscribers.

## Example Flow:
1. Client sends:
`stompClient.send("app/sendMessage", {}, JSON.stringify({sender: "John", content: "Hello!"}))
2. Server receives it sendMessage(), returns the message.
3. All clients subscribe to /topic/messages receive it.

## HTTP Handler Method
```java
@GetMapping("chat")
public String chat() {
  return "chat";
}
```
This method handles standard HTTP GET requests.

* `@GetMapping("chat")
 * When a browser accesses `http://localhost:8080/chat`, this method is called.
 * Returns `"chat"`
 * Spring looks for a thymeleaf (or other template engine) view named chat.html in src/main/resources/templates

So this method serves the chat UI page.
