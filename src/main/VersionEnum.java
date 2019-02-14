package main;

public enum VersionEnum {
	VERSION("2.5.0");

	private final String version;

	VersionEnum(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
