package needsboth;

public class NeedsBoth {
	static DepSystem result;

	public static DepSystem parse(String input) {
		needs18.Guava18.sameThread().execute(() -> {
			result = needs17.Guava17.parse(DepSystem.class, input);
		});
		return result;
	}

	public static enum DepSystem {
		MAVEN, P2;

		public boolean supportsDiamondDependencyConflicts() {
			switch (this) {
			case MAVEN: return false;
			case P2:    return true;
			default:    throw new IllegalArgumentException("No such dependency management system.");
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(NeedsBoth.parse("MAVEN").name());
		System.out.println(NeedsBoth.parse("P2").name());
	}
}
