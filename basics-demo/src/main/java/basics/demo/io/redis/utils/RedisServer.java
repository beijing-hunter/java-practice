package basics.demo.io.redis.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisServer {

	private static Map<String, KeyValue> kvDic = new ConcurrentHashMap<String, KeyValue>();

	public static KeyValue getValue(String key) {
		return kvDic.get(key);
	}

	public static String setValue(KeyValue kv) {

		kvDic.put(kv.getKey(), kv);
		return "ok";
	}

	public static String delValue(KeyValue kv) {

		kvDic.remove(kv.getKey());
		return "ok";
	}

	public static RedisCommand analysis(String content) {

		System.out.println(content);
		String[] params = content.split("&");

		if (params.length <= 1) {
			return null;
		}

		if (params.length == 2) {
			return new RedisCommand(params[0].toUpperCase(), params[1].toUpperCase());
		}

		if (params.length == 3) {
			return new RedisCommand(params[0].toUpperCase(), params[1].toUpperCase(), params[2]);
		}

		return null;
	}

	public static String exec(RedisCommand command) {

		if (command == null || command.getName() == null) {
			return "fail";
		}

		if (command.getName().equals(CommandEnum.GET)) {
			KeyValue kv = getValue(command.getKeyValue().getKey());
			return kv == null ? "nil" : kv.getValue();
		}

		if (command.getName().equals(CommandEnum.SET)) {
			return setValue(command.getKeyValue());
		}

		if (command.getName().equals(CommandEnum.DEL)) {
			return delValue(command.getKeyValue());
		}

		return "fail";
	}
}
