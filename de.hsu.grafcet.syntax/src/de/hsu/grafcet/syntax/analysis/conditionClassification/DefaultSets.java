package de.hsu.grafcet.syntax.analysis.conditionClassification;

import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultSets {
	
	public static Set<AbstractSignalValue> getD_DD_1_DD_0() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.D);
				add(AbstractSignalValue.DD_1);
				add(AbstractSignalValue.DD_0);
			}
		};
	}	
	public static Set<AbstractSignalValue> getD_DD_1() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.D);
				add(AbstractSignalValue.DD_1);
			}
		};
	}
	public static Set<AbstractSignalValue> getD_DD_0() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.D);
				add(AbstractSignalValue.DD_0);
			}
		};
	}
	public static Set<AbstractSignalValue> getDD_1_DD_0() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.DD_1);
				add(AbstractSignalValue.DD_0);
			}
		};
	}
	/**
	 * @deprecated
	 * same result as getD()
	 */
	public static Set<AbstractSignalValue> getD_D() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.D);
				add(AbstractSignalValue.D);
			}
		};
	}
	/**
	 * @deprecated
	 * same result as getDD_1()
	 */
	public static Set<AbstractSignalValue> getDD_1_DD_1() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.DD_1);
				add(AbstractSignalValue.DD_1);
			}
		};
	}
	/**
	 * @deprecated
	 * same result as getDD_0()
	 */
	public static Set<AbstractSignalValue> getDD_0_DD_0() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.DD_0);
				add(AbstractSignalValue.DD_0);
			}
		};
	}
	public static Set<AbstractSignalValue> getD() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.D);
			}
		};
	}
	public static Set<AbstractSignalValue> getDD_1() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.DD_1);
			}
		};
	}
	public static Set<AbstractSignalValue> getDD_0() {
		return new LinkedHashSet<AbstractSignalValue>() {
			{
				add(AbstractSignalValue.DD_0);
			}
		};
	}
	public static Set<AbstractSignalValue> getEmptySet() {
		return new LinkedHashSet<AbstractSignalValue>();
	}
}
