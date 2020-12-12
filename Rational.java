package MeresaMeron_Lab2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Meron
 */
public class Rational extends Number implements Comparable<Rational> {

	/** representing the value 0 */
	public static final Rational ZERO = new Rational();

	/** representing the value 1 */
	public static final Rational ONE = new Rational(1L);

	/** static string for 0 */
	protected static final String STRING_ZERO = "0";

	/** static string for 1 */
	protected static final String STRING_ONE = "1";

	/** pattern for validation of strings for parsing */
	protected static final Pattern STRING_PATTERN = Pattern.compile("^\\s*(-?\\d+)\\s*(?:/\\s*(-?\\d+))?\\s*$");

	private static final long serialVersionUID = 1L;

	/** *  The numerator */
	protected final BigInteger numerator;

	/** The denominator */
	protected final BigInteger denominator;

	/**
	 * The numerator of the reduced Rational, which also holds the sign.
	 */
	protected final BigInteger lowestNumerator;

	/** *  The denominator of the reduced Rational, always positive. */
	protected final BigInteger lowestDenominator;

	/**
	 * The greatest common divisor numerator denominator
	 * numerator and denominator are divided to create lowestNumerator and lowestDenominator}
	 */
	protected final BigInteger greatestCommonDivisor;

	/** The cached string value */
	protected final String stringValue;

	/** The cached reduced string value */
	protected final String reducedStringValue;

	/** The cached decimal string value */
	

	/** The cached hashCode */
	protected final int hashCode;

	/**
	 * Creates a new instance 
	 * 
	 */
	protected Rational() {
		numerator = BigInteger.ZERO;
		denominator = BigInteger.ONE;
		greatestCommonDivisor = BigInteger.ONE;
		lowestNumerator = numerator;
		lowestDenominator = denominator;
		stringValue = STRING_ZERO;
		reducedStringValue = STRING_ZERO;
		
		hashCode = calculateHashCode();
	}

	/**
	 * Creates a new instance that represents the value of double value.
	 *
	 * @param value the value.
	 */
	public Rational(double value) {
		this(BigDecimal.valueOf(value));
	}

	/**
	 * Creates a new instance that represents the value of BigDecimal value}.
	 *
	 * @param value the value.
	 */
	public Rational(BigDecimal value) {
		if (value.signum() == 0) {
			this.numerator = BigInteger.ZERO;
			denominator = BigInteger.ONE;
		} else {
			if (value.scale() > 0) {
				BigInteger unscaled = value.unscaledValue();
				BigInteger tempDenominator = BigInteger.TEN.pow(value.scale());
				BigInteger tempGreatestCommonDivisor = unscaled.gcd(tempDenominator);
				numerator = unscaled.divide(tempGreatestCommonDivisor);
				denominator = tempDenominator.divide(tempGreatestCommonDivisor);
			} else {
				numerator = value.toBigIntegerExact();
				denominator = BigInteger.ONE;
			}
		}
		greatestCommonDivisor = BigInteger.ONE;
		lowestNumerator = numerator;
		lowestDenominator = denominator;
		stringValue = toRationalString(numerator, denominator);
		reducedStringValue = stringValue;
		
		hashCode = calculateHashCode();
	} 

	/**
	 * Creates a new instance by parsing value. This has to be
	 * either integer numerator/integer denominator or
	 * integer numerator for the parsing to succeed. Signs are
	 * understood for both numerator and denominator. If value is blank
	 * or null a  Rational instance representing zero is
	 * created. If  value can't be parsed, a
	 *  NumberFormatException is thrown.
	 *
	 * @param value the  String value to parse.
	 * @throws NumberFormatException If value cannot be parsed.
	 */
	public Rational (String value) {
		if (isBlank(value)) {
			numerator = BigInteger.ZERO;
			denominator = BigInteger.ONE;
			greatestCommonDivisor = BigInteger.ONE;
			lowestNumerator = numerator;
			lowestDenominator = denominator;
			stringValue = STRING_ZERO;
			reducedStringValue = STRING_ZERO;
			
		} else {
			Matcher matcher = STRING_PATTERN.matcher(value);
			if (!matcher.find()) {
				throw new NumberFormatException(
					"Invalid value \"" + value +
					"\". The value must be either \"integer/integer\" or \"integer\""
				);
			}
			if (value.indexOf("/") > 0) {
				if (matcher.group(2).startsWith("-")) {
					// Keep the signum in the numerator
					numerator = new BigInteger(matcher.group(1)).negate();
					denominator = new BigInteger(matcher.group(2)).negate();
				} else {
					numerator = new BigInteger(matcher.group(1));
					denominator = new BigInteger(matcher.group(2));
				}
				greatestCommonDivisor = calculateGreatestCommonDivisor(numerator, denominator);
				lowestNumerator = numerator.divide(greatestCommonDivisor);
				lowestDenominator = denominator.divide(greatestCommonDivisor);
				stringValue = toRationalString(numerator, denominator);
				reducedStringValue = toRationalString(lowestNumerator, lowestDenominator);
			} else {
				numerator = new BigInteger(matcher.group(1));
				denominator = BigInteger.ONE;
				greatestCommonDivisor = BigInteger.ONE;
				lowestNumerator = numerator;
				lowestDenominator = denominator;
				stringValue = toRationalString(numerator, denominator);
				reducedStringValue = stringValue;
			}
			
		}
		hashCode = calculateHashCode();
	}  

	/**
	 * Creates a new instance that represents the value of int value.
	 *
	 * @param value the value.
	 */
	public Rational(int value) {
		this((long) value);
	}

	/**
	 * Creates a new instance that represents the value of value.
	 *
	 * @param value the value.
	 */
	public Rational(long value) {
		numerator = BigInteger.valueOf(value);
		denominator = BigInteger.ONE;
		greatestCommonDivisor = BigInteger.ONE;
		lowestNumerator = numerator;
		lowestDenominator = denominator;
		stringValue = Long.toString(value);
		reducedStringValue = stringValue;
		
		hashCode = calculateHashCode();
	}

	/**
	 * Creates a new instance that represents the value of BigIntegervalue.
	 *
	 * @param value the value.
	 */
	public Rational(BigInteger value) {
		numerator = value;
		denominator = BigInteger.ONE;
		greatestCommonDivisor = BigInteger.ONE;
		lowestNumerator = numerator;
		lowestDenominator = denominator;
		stringValue = toRationalString(numerator, denominator);
		reducedStringValue = stringValue;
		
		hashCode = calculateHashCode();
                
	}

	/**
	 * Creates a new instance with the given numerator 
	 * denominator
	 *
	 * @param numerator the numerator.
	 * @param denominator the denominator.
	 */
	public Rational(int numerator, int denominator) {
		this((long) numerator, (long) denominator);
                
	}

	/**
	 * Creates a new instance with long numerator
	 * denominator
	 *
	 * @param numerator the numerator.
	 * @param denominator the denominator.
	 */
	public Rational(long numerator, long denominator) {
		if (denominator == 0) {
			throw new IllegalArgumentException("denominator can't be zero");
		}
		if (numerator == 0) {
			this.numerator = BigInteger.ZERO;
			this.denominator = BigInteger.ONE;
		} else if (denominator < 0) {
			this.numerator = BigInteger.valueOf(-numerator);
			this.denominator = BigInteger.valueOf(-denominator);
		} else {
			this.numerator = BigInteger.valueOf(numerator);
			this.denominator = BigInteger.valueOf(denominator);
		}
		stringValue = toRationalString(this.numerator, this.denominator);
		long gcd = calculateGreatestCommonDivisor(numerator, denominator);
		this.greatestCommonDivisor = BigInteger.valueOf(gcd);
		if (gcd == 1) {
			this.lowestNumerator = this.numerator;
			this.lowestDenominator = this.denominator;
			reducedStringValue = stringValue;
		} else {
			this.lowestNumerator = this.numerator.divide(this.greatestCommonDivisor);
			this.lowestDenominator = this.denominator.divide(this.greatestCommonDivisor);
			reducedStringValue = toRationalString(this.lowestNumerator, this.lowestDenominator);
		}
		
		hashCode = calculateHashCode();
	}

	/**
	 * Creates a new instance with BigInteger numerator and
	 * denominator
	 *
	 * @param numerator the numerator.
	 * @param denominator the denominator.
	 */
	public Rational(BigInteger numerator, BigInteger denominator) {
            
		if (denominator == null || denominator.signum() == 0) {
			throw new IllegalArgumentException("denominator can't be zero");
		}
		if (numerator == null || numerator.signum() == 0) {
			this.numerator = BigInteger.ZERO;
			this.denominator = BigInteger.ONE;
		} else if (denominator.signum() < 0) {
			this.numerator = numerator.negate();
			this.denominator = denominator.negate();
		} else {
			this.numerator = numerator;
			this.denominator = denominator;
                        
		}
                
		stringValue = toRationalString(this.numerator, this.denominator);

		this.greatestCommonDivisor = calculateGreatestCommonDivisor(this.numerator, this.denominator);
                
		if (BigInteger.ONE.equals(this.greatestCommonDivisor)) {
			this.lowestNumerator = this.numerator;
			this.lowestDenominator = this.denominator;
			reducedStringValue = stringValue;
		} else {
			this.lowestNumerator = this.numerator.divide(this.greatestCommonDivisor);
			this.lowestDenominator = this.denominator.divide(this.greatestCommonDivisor);
			reducedStringValue = toRationalString(this.lowestNumerator, this.lowestDenominator);
		}
		
		hashCode = calculateHashCode();
                
	}


	// Operations


	/**
	 * Returns a Rational whose value is (this * value).
	 *
	 * @param val the  int value to be multiplied by this  Rational.
	 * @return The multiplication result.
	 */
	public Rational multiply(int val) {
		return multiply(BigInteger.valueOf(val));
	}

	/**
	 * Returns a  Rational whose value is  (this * value)
	 *
	 * @param val the  long value to be multiplied by this Rational.
	 * @return The multiplication result.
	 */
	public Rational multiply(long val) {
		return multiply(BigInteger.valueOf(val));
	}

	/**
	 * Returns a Rational whose value is code (this * value).
	 *
	 * @param val the BigInteger value to be multiplied by this link Rational.
	 * @return The multiplication result.
	 */
	public Rational multiply(BigInteger val) {
		if (val == null || val.signum() == 0) {
			return ZERO;
		}
		if (BigInteger.ONE.equals(val.abs())) {
			return val.signum() < 0 ? negate() : this;
		}

		return new Rational(lowestNumerator.multiply(val), lowestDenominator);
	}

	/**
	 * Returns a Rational whose value is  (this * value).
	 *
	 * @param val the  double value to be multiplied by this Rational.
	 * @return The multiplication result.
	 */
	public Rational multiply(double val) {
		return multiply(new Rational(val));
	}

	/**
	 * Returns a Rational whose value is (this * value).
	 *
	 * @param val the BigDecimal value to be multiplied by this Rational.
	 * @return The multiplication result.
	 */
	public Rational multiply(BigDecimal val) {
		return multiply(new Rational(val));
	}

	/**
	 * Returns a  Rational whose value is (this * value).
	 *
	 * @param val the Rational value to be multiplied by this Rational.
	 * @return The multiplication result.
	 */
	public Rational multiply(Rational val) {
		if (val == null || val.numerator.signum() == 0) {
			return ZERO;
		}
		if (val.numerator.equals(val.denominator)) {
			return val.numerator.signum() < 0 ? this.negate() : this;
		}

		BigInteger newNumerator = lowestNumerator.multiply(val.lowestNumerator);
		BigInteger newDenominator = lowestDenominator.multiply(val.lowestDenominator);
		BigInteger gcd = newNumerator.gcd(newDenominator);
		return new Rational(newNumerator.divide(gcd), newDenominator.divide(gcd));
	}

	/**
	 * Returns a  Rational whose value is {(this - value).
	 *
	 * @param subtrahend the  int value to be subtracted from this Rational.
	 * @return The subtraction result.
	 */
	public Rational subtract(int subtrahend) {
		return add(-subtrahend);
	}

	/**
	 * Returns a Rational whose value is (this - value).
	 *
	 * @param subtrahend the long value to be subtracted from this Rational.
	 * @return The subtraction result.
	 */
	public Rational subtract(long subtrahend) {
		return add(-subtrahend);
	}

	/**
	 * Returns Rational whose value is (this - value).
	 *
	 * @param subtrahend the BigInteger value to be subtracted from this Rational.
	 * @return The subtraction result.
	 */
	public Rational subtract(BigInteger subtrahend) {
		return add(subtrahend.negate());
	}

	/**
	 * Returns a Rational whose value is (this - value).
	 *
	 * @param subtrahend the double value to be subtracted from this Rational.
	 * @return The subtraction result.
	 */
	public Rational subtract(double subtrahend) {
		return add(-subtrahend);
	}

	/**
	 * Returns a Rational whose value is (this - value).
	 *
	 * @param subtrahend the BigDecimal value to be subtracted from this Rational.
	 * @return The subtraction result.
	 */
	public Rational subtract(BigDecimal subtrahend) {
		return add(subtrahend.negate());
	}

	/**
	 * Returns a Rational whose value is code (this - value).
	 *
	 * @param subtrahend the Rational value to be subtracted from this Rational.
	 * @return The subtraction result.
	 */
	public Rational subtract(Rational subtrahend) {
		if (subtrahend == null || subtrahend.numerator.signum() == 0) {
			return this;
		}
		return add(subtrahend.negate());
	}

	/**
	 * Returns a Rational whose value is code (this + value).
	 *
	 * @param addend the int value to be added to this Rational.
	 * @return The addition result.
	 */
	public Rational add(int addend) {
		return add((long) addend);
	}

	/**
	 * Returns a Rational whose value is (this + value).
	 *
	 * @param addend the long value to be added to this Rational.
	 * @return The addition result.
	 */
	public Rational add(long addend) {
		return add(BigInteger.valueOf(addend));
	}

	/**
	 * Returns a Rational whose value is (this + value).
	 *
	 * @param addend the BigInteger value to be added to this Rational.
	 * @return The addition result.
	 */
	public Rational add(BigInteger addend) {
		if (addend == null || addend.signum() == 0) {
			return this;
		}
		if (BigInteger.ONE.equals(denominator)) {
			return new Rational(numerator.add(addend), denominator);
		}
		return new Rational(numerator.add(addend.multiply(denominator)), denominator);
	}

	/**
	 * Returns a Rational whose value is (this + value).
	 *
	 * @param addend the double value to be added to this Rational.
	 * @return The addition result.
	 */
	public Rational add(double addend) {
		return add(new Rational(addend));
	}

	/**
	 * Returns a Rational whose value is (this + value).
	 *
	 * @param addend the BigDecimal value to be added to this Rational.
	 * @return The addition result.
	 */
	public Rational add(BigDecimal addend) {
		return add(new Rational(addend));
	}

	/**
	 * Returns a Rational whose value is (this + value).
	 *
	 * @param addend the  Rational value to be added to this Rational.
	 * @return The addition result.
	 */
	public Rational add(Rational addend) {
		if (addend == null || addend.numerator.signum() == 0) {
			return this;
		}
		if (this.numerator.signum() == 0) {
			return addend;
		}
		BigInteger lcm = calculateLeastCommonMultiple(denominator, addend.denominator);
		return new Rational(numerator.multiply(lcm.divide(denominator)).add(addend.numerator.multiply(lcm.divide(addend.denominator))), lcm);
	}

	/**
	 * Returns a Rational whose value is the reciprocal of this
	 * (1 / this).
	 *
	 * @return The reciprocal result.
	 */
	public Rational reciprocal() {
		return numerator.signum() == 0 ? this : new Rational(denominator, numerator);
	}

	/**
	 * Returns a Rational whose value is (this / value).
	 *
	 * @param divisor the  int value by which this  Rational is to be divided.
	 * @return The division result.
	 * @throws IllegalArgumentException if value is zero.
	 */
	public Rational divide(int divisor) {
		return divide((long) divisor);
	}

	/**
	 * Returns a Rational whose value is (this / value).
	 *
	 * @param divisor the long value by which this Rational is to be divided.
	 * @return The division result.
	 * @throws IllegalArgumentException if value is zero.
	 */
	public Rational divide(long divisor) {
		if (divisor == 0) {
			throw new IllegalArgumentException("value cannot be zero/divison by zero");
		}
		if (numerator.signum() == 0 || divisor == 1) {
			return this;
		}
		if (divisor == -1) {
			return negate();
		}

		// Keep the sign in the numerator and the denominator positive
		if (divisor < 0) {
			return new Rational(lowestNumerator.negate(), lowestDenominator.multiply(BigInteger.valueOf(-divisor)));
		}
		return new Rational(lowestNumerator, lowestDenominator.multiply(BigInteger.valueOf(divisor)));
	}

	/**
	 * Returns a Rational whose value is (this / value).
	 *
	 * @param divisor the BigInteger value by which this Rational is to be divided.
	 * @return The division result.
	 * @throws IllegalArgumentException if value is zero.
	 */
	public Rational divide(BigInteger divisor) {
		if (divisor == null || divisor.signum() == 0) {
			throw new IllegalArgumentException("value cannot be zero/divison by zero");
		}
		if (numerator.signum() == 0) {
			return this;
		}
		if (BigInteger.ONE.equals(divisor.abs())) {
			return divisor.signum() < 0 ? negate() : this;
		}

		// Keep the sign in the numerator and the denominator positive
		if (divisor.signum() < 0) {
			return new Rational(lowestNumerator.negate(), lowestDenominator.multiply(divisor.negate()));
		}
		return new Rational(lowestNumerator, lowestDenominator.multiply(divisor));
	}

	/**
	 * Returns a Rational whose value is (this / value).
	 *
	 * @param divisor the  double value by which this Rational is to be divided.
	 * @return The division result.
	 * @throws IllegalArgumentException if value is zero.
	 */
	public Rational divide(double divisor) {
		if (divisor == 0) {
			throw new IllegalArgumentException("value cannot be zero/divison by zero");
		}
		if (numerator.signum() == 0 || divisor == 1) {
			return this;
		}
		if (divisor == -1) {
			return negate();
		}
		return divide(new Rational(divisor));
	}

	/**
	 * Returns a Rational whose value is  (this / value)}.
	 *
	 * @param divisor the BigDecimal value by which this Rational is to be divided.
	 * @return The division result.
	 * @throws IllegalArgumentException if value is zero.
	 */
	public Rational divide(BigDecimal divisor) {
		if (divisor == null || divisor.signum() == 0) {
			throw new IllegalArgumentException("value cannot be zero/divison by zero");
		}
		if (numerator.signum() == 0 ||  BigDecimal.ONE.equals(divisor.abs())) {
			return divisor.signum() < 0 ? negate() : this;
		}
		return divide(new Rational(divisor));
	}

	/**
	 * Returns a Rational whose value is (this / value).
	 *
	 * @param divisor the  Rational value by which this Rational is to be divided.
	 * @return The division result.
	 * @throws IllegalArgumentException if value is zero.
	 */
	public Rational divide(Rational divisor) {
		if (divisor == null || divisor.numerator.signum() == 0) {
			throw new IllegalArgumentException("value cannot be zero/divison by zero");
		}
		return numerator.signum() == 0 ? this : multiply(divisor.reciprocal());
	}

	/**
	 * Returns a  Rational whose value is (-this).
	 *
	 * @return The negated result.
	 */
	public Rational negate() {
		if (numerator.signum() == 0) {
			return this;
		}
		return new Rational(numerator.negate(), denominator);
	}

	/**
	 * Returns a Rational whose value is the absolute value of this
	 *  (abs(this)).
	 *
	 * @return The absolute result.
	 */
	public Rational abs() {
		return numerator.signum() < 0 ? new Rational(numerator.negate(), denominator) : this;
	}

	/**
	 * Returns a Rational whose value is
	 * this^(exponent)
	 *
	 * @param exponent exponent to which this Rational is to be raised.
	 * @return this^(exponent)
	 */
	public Rational pow(int exponent) {
		if (exponent == 0) {
			return ONE;
		}
		if (exponent == 1) {
			return this;
		}
		if (exponent < 0) {
			if (exponent == Integer.MIN_VALUE) {
				return this.reciprocal().pow(2).pow(-(exponent / 2));
			}
			return this.reciprocal().pow(-exponent);
		}
		Rational result = multiply(this);
		if ((exponent % 2) == 0) {
			return result.pow(exponent / 2);
		}
		return result.pow(exponent / 2).multiply(this);
	}

	/**
	 * Returns the signum function of this Rational.
	 *
	 * @return -1, 0 or 1 as the value of this Rational is negative,
	 *         zero or positive.
	 */
	public int signum() {
		return numerator.signum();
	}

	/**
	 * @return Whether the value of this  Rational can be expressed as an
	 *         integer value.
	 */
	public boolean isInteger() {
		return BigInteger.ONE.equals(lowestDenominator);
	}


	// Getters


	/**
	 * @return The numerator.
	 */
	public BigInteger getNumerator() {
		return numerator;
	}

	/**
	 * @return The denominator.
	 */
	public BigInteger getDenominator() {
		return denominator;
	}

	/**
	 * The reduced numerator is the numerator divided by
	 * getGreatestCommonDivisor
	 *
	 * @return The reduced numerator.
	 */
	public BigInteger getReducedNumerator() {
		return lowestNumerator;
	}

	/**
	 * The reduced denominator is the denominator divided by
	 * getGreatestCommonDivisor
	 *
	 * @return The reduced denominator.
	 */
	public BigInteger getReducedDenominator() {
		return lowestDenominator;
	}

	/**
	 * @return the greatest common divisor of the numerator and the denominator.
	 */
	public BigInteger getGreatestCommonDivisor() {
		return greatestCommonDivisor;
	}


	// String methods


	/**
	 * Returns a string representation of this Rational in it's rational
	 * form  (1/2, 4 or 16/9).
	 *
	 * @return The String representation.
	 */
	@Override
	public String toString() {
		return stringValue;
	}


	/**
	 * Returns a string representation of this Rational in it's reduced
	 * rational form (1/2, 4 or 16/9) The reduced form is when both
	 * numerator and denominator have been divided by the greatest common
	 * divisor.
	 *
	 * @return The reduced String representation.
	 */
	public String toReducedString() {
		return reducedStringValue;
	}

	


	// Conversion to other Number formats


	/**
	 * Converts this Rational to an int. 
	 * 
	 *
	 * @return This  Rational converted to an int.
	 */
	@Override
	public int intValue() {
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), RoundingMode.DOWN).intValue();
	}

	/**
	 *
	 * Converts this  Rational to a  long. 
	 *
	 * @return This Rational converted to a  long.
	 */
	@Override
	public long longValue() {
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), RoundingMode.DOWN).longValue();
	}

	/**
	 * Converts this Rational to a  BigInteger. 
	 *
	 * @return This  Rational converted to a BigInteger.
	 */
	public BigInteger bigIntegerValue() {
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), RoundingMode.DOWN).toBigInteger();
	}

	/**
	 * Converts this  Rational to a  float. 
	 *
	 * @return This  Rational converted to a float.
	 */
	@Override
	public float floatValue() {
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), MathContext.DECIMAL32).floatValue();
	}

	/**
	 * Converts this  Rational to a double. 
	 *
	 * @return This  Rational converted to a  double.
	 */
	@Override
	public double doubleValue() {
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), MathContext.DECIMAL64).doubleValue();
	}

	/**
	 * Converts this  Rational to a  BigDecimal. 
	 *
	 * @see #bigDecimalValue(MathContext)
	 * @see #bigDecimalValue(RoundingMode)
	 * @see #bigDecimalValue(int, RoundingMode)
	 *
	 * @return This Rational converted to a BigDecimal
	 */
	public BigDecimal bigDecimalValue() {
		if (BigInteger.ONE.equals(lowestDenominator)) {
			return new BigDecimal(lowestNumerator);
		}
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), 100, RoundingMode.HALF_EVEN);
	}

	/**
	 * Converts this Rational to a  BigDecimal. This may involve
	 * rounding. The conversion is limited to 100 decimals and uses
	 * roundingMode.
	 *
	 * @see #bigDecimalValue()
	 * @see #bigDecimalValue(MathContext)
	 * @see #bigDecimalValue(int, RoundingMode)
	 *
	 * @param roundingMode the  RoundingMode to apply.
	 * @return This Rational converted to a  BigDecimal.
	 */
	public BigDecimal bigDecimalValue(RoundingMode roundingMode) {
		if (BigInteger.ONE.equals(lowestDenominator)) {
			return new BigDecimal(lowestNumerator);
		}
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), 100, roundingMode);
	}

	/**
	 * Converts this Rational to a  BigDecimal. This may involve
	 * rounding.
	 * 
	 *
	 * @see #bigDecimalValue()
	 * @see #bigDecimalValue(MathContext)
	 * @see #bigDecimalValue(RoundingMode)
	 *
	 * @param scale the scale of the BigDecimal quotient to be returned.
	 * @param roundingMode the  RoundingMode to apply.
	 * @return This  Rational converted to a  BigDecimal.
	 * @throws ArithmeticException If
	 *             roundingMode == RoundingMode.UNNECESSARY and the
	 *             specified scale is insufficient to represent the result of
	 *             the division exactly.
	 */
	public BigDecimal bigDecimalValue(int scale, RoundingMode roundingMode) {
		if (BigInteger.ONE.equals(lowestDenominator)) {
			return new BigDecimal(lowestNumerator);
		}
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), scale, roundingMode);
	}

	/**
	 * Converts this  Rational to a  BigDecimal using the given
	 *  MathContext. 
	 * 
	 *
	 * @see #bigDecimalValue()
	 * @see #bigDecimalValue(RoundingMode)
	 * @see #bigDecimalValue(int, RoundingMode)
	 *
	 * @param mathContext the {@link MathContext} to use.
	 * @return This  Rational converted to a  BigDecimal.
	 * @throws ArithmeticException If the result is inexact but the rounding
	 *             mode is  UNNECESSARY} or
	 *              mathContext.precision == 0} and the quotient has a
	 *             non-terminating decimal expansion.
	 */
	public BigDecimal bigDecimalValue(MathContext mathContext) {
		if (BigInteger.ONE.equals(lowestDenominator)) {
			return new BigDecimal(lowestNumerator);
		}
		return new BigDecimal(lowestNumerator).divide(new BigDecimal(lowestDenominator), mathContext);
	}


	// Comparison methods


	/**
	 * Compares this  Rational with  other. Two Rational
	 * instances that are equal in value but have different numerators and
	 * denominators are equal by this methods 
	 *
	 *
	 * @param val the  Rational to which this  Rational is to be
	 *            compared.
	 * @return A negative integer, zero, or a positive integer as this
	 *         Rational is numerically less than, equal to, or greater
	 *         than  other.
	 */
	@Override
	public int compareTo(Rational val) {
		if (signum() != val.signum()) {
			return signum() - val.signum();
		}
		BigInteger[] multipliers = getMultipliers(val);
		return lowestNumerator.multiply(multipliers[0]).compareTo(val.lowestNumerator.multiply(multipliers[1]));
	}

	/**
	 * Compares this Rational by value with a  int.
	 * 
	 *
	 * @param value the  int to which this  Rational's value is to
	 *            be compared.
	 * @return A negative integer, zero, or a positive integer as this
	 *         Rational is numerically less than, equal to, or greater
	 *         than  value.
	 */
	public int compareTo(int value) {
		return compareTo(Integer.valueOf(value));
	}

	/**
	 * Compares this  Rational by value with a long.
	 * 
	 *
	 * @param value the  long to which this  Rational's value
	 *            is to be compared.
	 * @return A negative integer, zero, or a positive integer as this
	 *          Rational is numerically less than, equal to, or greater
	 *         than value.
	 */
	public int compareTo(long value) {
		return compareTo(Long.valueOf(value));
	}

	/**
	 * Compares this  Rational by value with a  float.
	 * 
	 *
	 * @param value the  float to which this  Rational's value
	 *            is to be compared.
	 * @return A negative integer, zero, or a positive integer as this
	 *          Rational is numerically less than, equal to, or greater
	 *         than  value.
	 */
	public int compareTo(float value) {
		return compareTo(Float.valueOf(value));
	}

	/**
	 * Compares this  Rational by value with a  double.
	 * 
	 *
	 * @param value the  double to which this  Rational's value
	 *            is to be compared.
	 * @return A negative integer, zero, or a positive integer as this
	 *          Rational is numerically less than, equal to, or greater
	 *         than  value.
	 */
	public int compareTo(double value) {
		return compareTo(Double.valueOf(value));
	}

	/**
	 * Compares this Rational by value with any class implementing
	 *  Number.
	 * 
	 *
	 * @param number the Number to which this  Rational's value
	 *            is to be compared.
	 * @return A negative integer, zero, or a positive integer as this
	 *         Rational is numerically less than, equal to, or greater
	 *         than  number.
	 */
	public int compareTo(Number number) {
		// List known integer types for faster and more accurate comparison
		if (number instanceof BigInteger) {
			if (isInteger()) {
				return bigIntegerValue().compareTo((BigInteger) number);
			}
			return bigDecimalValue(2, RoundingMode.HALF_EVEN).compareTo(new BigDecimal((BigInteger) number));
		}
		if (
			number instanceof AtomicInteger ||
			number instanceof AtomicLong ||
			number instanceof Byte ||
			number instanceof Integer ||
			number instanceof Long ||
			number instanceof Short
		) {
			if (isInteger()) {
				return bigIntegerValue().compareTo(BigInteger.valueOf(number.longValue()));
			}
			return bigDecimalValue(2, RoundingMode.HALF_EVEN).compareTo(new BigDecimal(number.longValue()));
		}
		if (number instanceof BigDecimal) {
			Rational other = new Rational((BigDecimal) number);
			return compareTo(other);
		}
		return bigDecimalValue().compareTo(new BigDecimal(number.doubleValue()));
	}
           static boolean isBlank(String value) {
    return value == null || value.trim().length() == 0;
}

	@Override
	public int hashCode() {
		return hashCode;
	}

	/**
	 * Used internally to calculate the  hashCode for caching.
	 *
	 * @return The calculated  hashCode.
	 */
	protected int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lowestDenominator == null) ? 0 : lowestDenominator.hashCode());
		result = prime * result + ((lowestNumerator == null) ? 0 : lowestNumerator.hashCode());
		return result;
	}


	/**
	 * Confirms whether this instance and  object are mathematically
	 * equivalent, given that  other implements  Number.	 * 
	 * @param object the reference object with which to compare.
	 * @return  true if  object is a  Rational and are
	 *         mathematically equivalent,  false otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (!(object instanceof Number)) {
			return false;
		}
		if (object instanceof Rational) {
			Rational other = (Rational) object;
			if (lowestDenominator == null) {
				if (other.lowestDenominator != null) {
					return false;
				}
			} else if (!lowestDenominator.equals(other.lowestDenominator)) {
				return false;
			}
			if (lowestNumerator == null) {
				if (other.lowestNumerator != null) {
					return false;
				}
			} else if (!lowestNumerator.equals(other.lowestNumerator)) {
				return false;
			}
		} else {
			if (lowestNumerator == null || lowestDenominator == null) {
				// Should be impossible
				return false;
			}
			return compareTo((Number) object) == 0;
		}
		return true;
	}

	/**
	 * Indicates whether this instance and other have identical
	 * numerator and denominator.
	 * @param other the  Rational with which to compare.
	 * @return  true if this instance and  other have an identical
	 *         representation.
	 */
	public boolean equalsExact(Rational other) {
		return other != null && numerator.equals(other.numerator) && denominator.equals(other.denominator);
	}

	/**
	 * Used internally to find by which factor to multiply the reduced
	 * numerators when comparing two  Rationals.
	 *
	 * @param other the  Rational to which this  Rational's value
	 *            is to be compared.
	 * @return An array of BigInteger multipliers.
	 */
	protected BigInteger[] getMultipliers(Rational other) {
		BigInteger[] result = new BigInteger[2];
		BigInteger lcm = calculateLeastCommonMultiple(lowestDenominator, other.lowestDenominator);
		result[0] = lcm.divide(lowestDenominator);
		result[1] = lcm.divide(other.lowestDenominator);
		return result;
	}


	// Static methods


	/**
	 * Calculates the greatest common divisor for two  Longs using		 *
	 * @param h the first number.
	 * @param o the second number.
	 * @return The GDC, always 1 or greater.
	 */
	public static long calculateGreatestCommonDivisor(long h, long o) {
		if (Math.abs(h) <= 1 || Math.abs(o) <= 1) {
			return 1;
		}
		// keep u and v negative, as negative integers range down to
		// -2^63, while positive numbers can only be as large as 2^63-1.
		if (h > 0) {
			h = -h;
		}
		if (o > 0) {
			o = -o;
		}
		int p = 0;
		while ((h & 1) == 0 && (o & 1) == 0 && p < 63) {
			h >>= 1;
			o >>= 1;
			p++;
		}
		if (p == 63) {
			throw new ArithmeticException("Overflow: gcd is 2^63");
		}
		long t = ((h & 1) == 1) ? o : -(h >> 1);
		do {
			while ((t & 1) == 0) {
				t >>= 1;
			}
			if (t > 0) {
				h = -t;
			} else {
				o = t;
			}
			t = (o - h) >> 1;
		} while (t != 0);

		return -h * (1 << p);
	}

	/**
	 * Calculates the greatest common divisor for two BigIntegers using
	 * BigInteger#gcd.
	 *
	 * @param n the first number.
	 * @param p the second number.
	 * @return The GDC, always 1 or greater.
	 */
	public static BigInteger calculateGreatestCommonDivisor(BigInteger n, BigInteger p) {
		if (n.abs().compareTo(BigInteger.ONE) <= 0 || p.abs().compareTo(BigInteger.ONE) <= 0) {
			return BigInteger.ONE;
		}
		return n.gcd(p);
	}

	/**
	 * Calculates the least common multiple for two BigIntegers using the formula
	 * u * v / gcd(u, v) where gcd is the greatest common divisor for the two.
	 *
	 * @param g the first number.
	 * @param o the second number.
	 * @return The LCM, always 1 or greater.
	 */
	public static BigInteger calculateLeastCommonMultiple(BigInteger g, BigInteger o) {
		if (g == null) {
			g = BigInteger.ZERO;
		}
		if (o == null) {
			o = BigInteger.ZERO;
		}
		if (g.signum() == 0 && o.signum() == 0) {
			return BigInteger.ONE;
		}
		g = g.abs();
		o = o.abs();
		if (g.signum() == 0) {
			return o;
		}
		if (o.signum() == 0) {
			return g;
		}
		return g.divide(calculateGreatestCommonDivisor(g, o)).multiply(o);
	}


	/**
	 * Used internally to generate a rational string representation from two
	 * BigIntegers.
	 *
	 * @param numerator the numerator.
	 * @param denominator the denominator.
	 * @return The rational string representation.
	 */
	 static String toRationalString(BigInteger numerator, BigInteger denominator) {
		if (BigInteger.ONE.equals(denominator)) {
			return numerator.toString();
		}
		return numerator.toString() + "/" + denominator.toString();
	}


          static Rational valueOf(long a) {
      BigInteger num = BigInteger.valueOf(a);
      BigInteger denom = BigInteger.ONE;
      return new Rational(num, denom);
          }
          
           public Rational valueOf(double a) {
        BigDecimal val = BigDecimal.valueOf(a);
        BigInteger num = val.unscaledValue();
        BigInteger denom = BigInteger.TEN;
        denom = denom.pow(val.scale());
        return new Rational(num, denom);
    }
           
           
   
           /** Return the minimal value of two BigRationals.
     * @param val
     * @return 
	*/
	public Rational minimum(Rational val)
	{
		return (compareTo(val) <= 0 ? this : val);
	}

	/** Return the minimal value of a BigRational and a long fix number integer.
     * @param val
     * @return 
	*/
	public Rational minimum(long val)
	{
		return minimum(new Rational(val));
	}

	/** *  An alias to minimum().[Name: see classes Math, BigInteger.]
     * @param val
     * @return 
	*/
	public Rational min(Rational val)
	{
		return minimum(val);
	}

	/** An alias to minimum().
     * @param val
     * @return 
	*/
	public Rational min(long val)
	{
		return minimum(val);
	}

	/** Return the maximal value of two BigRationals.
     * @param val
     * @return 
	*/
	public Rational maximum(Rational val)
	{
		return (compareTo(val) >= 0 ? this : val);
	}

	/** Return the maximum value of a BigRational and a long fix number integer.
     * @param val
     * @return 
	*/
	public Rational maximum(long val)
	{
		return maximum(new Rational(val));
	}
         public boolean isProper()     
         { return numerator.doubleValue() < denominator.doubleValue() 
         || numerator.longValue() < denominator.longValue() 
         || numerator.floatValue() < denominator.floatValue()
         || numerator.intValue() < denominator.intValue();
         
        
        }
         
                 

   
}


















































