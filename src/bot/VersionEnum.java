package bot;

public enum VersionEnum {
	VERSION("2.4.6");

	private final String version;

	VersionEnum(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
