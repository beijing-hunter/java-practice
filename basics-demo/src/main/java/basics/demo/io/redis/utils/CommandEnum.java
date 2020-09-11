package basics.demo.io.redis.utils;

public enum CommandEnum {

	GET("GET"), SET("SET"), DEL("DEL");

	private String cd;

	private CommandEnum(String cd) {
		this.cd = cd;
	}

	public String getCd() {
		return cd;
	}

	public static CommandEnum getCommand(String cd) {

		CommandEnum[] values = CommandEnum.values();

		for (CommandEnum item : values) {

			if (item.getCd().equals(cd)) {
				return item;
			}
		}

		return null;
	}
}
