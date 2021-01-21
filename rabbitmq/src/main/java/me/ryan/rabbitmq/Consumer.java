package me.ryan.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) {
        Address[] addresses = new Address[]{
                new Address("127.0.0.1", 5672)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            Connection connection = factory.newConnection(addresses);
            final Channel channel = connection.createChannel();
            channel.basicQos(64);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("Receive message: " + new String(body));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            channel.basicConsume("queue_demo", consumer);
            TimeUnit.SECONDS.sleep(5);
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
