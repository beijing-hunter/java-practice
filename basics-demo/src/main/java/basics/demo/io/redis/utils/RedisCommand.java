package basics.demo.io.redis.utils;

public class RedisCommand {

	private CommandEnum name;

	private KeyValue keyValue;

	public RedisCommand() {

	}

	public RedisCommand(String name, String key) {
		this(name, key, null);
	}

	public RedisCommand(String name, String key, String value) {
		super();
		this.name = CommandEnum.getCommand(name);
		this.keyValue = new KeyValue();
		this.keyValue.setKey(key);
		this.keyValue.setValue(value);
	}

	public CommandEnum getName() {
		return name;
	}

	public void setName(CommandEnum name) {
		this.name = name;
	}

	public KeyValue getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(KeyValue keyValue) {
		this.keyValue = keyValue;
	}
}
