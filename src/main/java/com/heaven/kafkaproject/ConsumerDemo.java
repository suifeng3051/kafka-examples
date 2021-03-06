package com.heaven.kafkaproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class ConsumerDemo {

	private final ConsumerConnector consumer;

	private final String topic;

	public ConsumerDemo(String zookeeper, String groupId, String topic) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "10000");
		props.put("zookeeper.sync.time.ms", "10000");
		props.put("auto.commit.interval.ms", "10000");
		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
		this.topic = topic;
	}

	public void testConsumer() {
		Map<String, Integer> topicCount = new HashMap<String, Integer>();
		// Define single thread for topic
		topicCount.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
		List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
		for (final KafkaStream stream : streams) {
			ConsumerIterator<byte[], byte[]> consumerIte = stream.iterator();
			while (consumerIte.hasNext()) {
				System.out.println("Message from Single Topic :: " + new String(consumerIte.next().message()));
			}
		}
		if (consumer != null) {
			consumer.shutdown();
		}
	}

	public static void main(String[] args) {
		String topic = "wanghouda";
		ConsumerDemo simpleHLConsumer = new ConsumerDemo("wanghouda:2181", "mygroup", topic);
		simpleHLConsumer.testConsumer();
	}
}
