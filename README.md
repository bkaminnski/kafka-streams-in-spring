# Kafka Streams in Spring

This project presents how to use Kafka Streams in Spring. The main ideas for this exercise were taken from *Designing Event-Driven Systems - Concepts and Patterns for Streaming Services with Apache Kafka* by Ben Stopford, a free e-book available for download [here](https://www.confluent.io/designing-event-driven-systems). Note that although main concepts do come from the book, details of the example as well as implementation details like Spring usage - may vary from what was presented in the book and its accompanying [github examples](https://github.com/confluentinc/kafka-streams-examples/tree/4.0.0-post/src/main/java/io/confluent/examples/streams/microservices).

## Projects

- `Orders service`
- `Payments service`
- `Customers service`
- `Notifications service`
- `Integration tests` which triggers test scenario and verifies results

## Plot

The story covered by this example starts after a customer buys a product and makes a payment. The payment itself is processed by a third party platform, which notifies `Payments service` about successful payment via a REST API. In reaction to the REST API call, `payments service` forms a `Payment message` and publishes it to the `payments topic`.

`Payment message`:
- payment id
- order id
- customer id
- payment date
- amount received
- payment type (PayPal, VISA, MasterCard)

`Orders service` subscribes to the `payments topic`. Once it receives a `payment message`, it checks whether the received amount covers total value of the order, and publishes an `Order` message to the `orders topic`.

`Order message`:
- order id
- customer id
- total value
- status = PAID or PAYMENT_MISMATCH

`Notifications service` listens to the stream of `payment messages` and `orders messages`, joins the stream together, and sends notification email. It is assumed, that `Payment message` and `Order message` are created at about the same time (5 minutes buffer is enough). In order to send an email, `notification service` needs to know customer name and email address. Since we do not want to make a call to `customers service` each time a notification is sent, `notification service` creates a local lookup table based on `customers topic`.

Each time a new customer is created, a `customers service` publishes a `customer message` on `customers topic`.

`Customer message`:
- customer id
- name
- email

## Technical remarks

- Examples to the book use Avro. This example uses JSON.