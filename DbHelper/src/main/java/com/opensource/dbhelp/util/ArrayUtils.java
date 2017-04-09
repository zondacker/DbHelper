/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.opensource.dbhelp.util;

import java.lang.reflect.Array;

/**
 * <p>
 * Operations on arrays, primitive arrays (like {@code int[]}) and primitive wrapper arrays (like {@code Integer[]}).
 * </p>
 * <p>
 * This class tries to handle {@code null} input gracefully. An exception will not be thrown for a {@code null} array input. However, an Object array that contains a {@code null} element may throw an exception. Each method documents its behaviour.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 * @since 2.0
 * @version $Id: ArrayUtils.java 1154216 2011-08-05 13:57:16Z mbenson $
 */
public class ArrayUtils {

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(null, null)     = null
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * ArrayUtils.addAll([null], [null]) = [null, null]
	 * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
	 * </pre>
	 *
	 * @param <T>
	 *            the component type of the array
	 * @param array1
	 *            the first array whose elements are added to the new array, may be {@code null}
	 * @param array2
	 *            the second array whose elements are added to the new array, may be {@code null}
	 * @return The new array, {@code null} if both arrays are {@code null}. The type of the new array is the type of the first array, unless the first array is null, in which case the type is the same as the second array.
	 * @since 2.1
	 * @throws IllegalArgumentException
	 *             if the array types are incompatible
	 */
	public static <T> T[] addAll(T[] array1, T... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		final Class<?> type1 = array1.getClass().getComponentType();
		@SuppressWarnings("unchecked")
		// OK, because array is of type T
		T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try {
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		}
		catch (ArrayStoreException ase) {
			// Check if problem was due to incompatible types
			/*
			 * We do this here, rather than before the copy because: - it would be a wasted check most of the time - safer, in case check turns out to be too strict
			 */
			final Class<?> type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2)) {
				throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
			}
			throw ase; // No, so rethrow original
		}
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new boolean[] array.
	 * @since 2.1
	 */
	public static boolean[] addAll(boolean[] array1, boolean... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		boolean[] joinedArray = new boolean[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new char[] array.
	 * @since 2.1
	 */
	public static char[] addAll(char[] array1, char... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		char[] joinedArray = new char[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new byte[] array.
	 * @since 2.1
	 */
	public static byte[] addAll(byte[] array1, byte... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new short[] array.
	 * @since 2.1
	 */
	public static short[] addAll(short[] array1, short... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		short[] joinedArray = new short[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new int[] array.
	 * @since 2.1
	 */
	public static int[] addAll(int[] array1, int... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		int[] joinedArray = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new long[] array.
	 * @since 2.1
	 */
	public static long[] addAll(long[] array1, long... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		long[] joinedArray = new long[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new float[] array.
	 * @since 2.1
	 */
	public static float[] addAll(float[] array1, float... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		float[] joinedArray = new float[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 *
	 * @param array1
	 *            the first array whose elements are added to the new array.
	 * @param array2
	 *            the second array whose elements are added to the new array.
	 * @return The new double[] array.
	 * @since 2.1
	 */
	public static double[] addAll(double[] array1, double... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		double[] joinedArray = new double[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element, unless the element itself is null, in which case the return type is Object[]
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, null)      = [null]
	 * ArrayUtils.add(null, "a")       = ["a"]
	 * ArrayUtils.add(["a"], null)     = ["a", null]
	 * ArrayUtils.add(["a"], "b")      = ["a", "b"]
	 * ArrayUtils.add(["a", "b"], "c") = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param <T>
	 *            the component type of the array
	 * @param array
	 *            the array to "add" the element to, may be {@code null}
	 * @param element
	 *            the object to add, may be {@code null}
	 * @return A new array containing the existing elements plus the new element The returned array type will be that of the input array (unless null), in which case it will have the same type as the element. If both are null, an IllegalArgumentException is thrown
	 * @since 2.1
	 * @throws IllegalArgumentException
	 *             if both arguments are null
	 */
	public static <T> T[] add(T[] array, T element) {
		Class<?> type;
		if (array != null) {
			type = array.getClass();
		} else if (element != null) {
			type = element.getClass();
		} else {
			throw new IllegalArgumentException("Arguments cannot both be null");
		}
		@SuppressWarnings("unchecked")
		// type must be T
		T[] newArray = (T[]) copyArrayGrow1(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, true)          = [true]
	 * ArrayUtils.add([true], false)       = [true, false]
	 * ArrayUtils.add([true, false], true) = [true, false, true]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static boolean[] add(boolean[] array, boolean element) {
		boolean[] newArray = (boolean[]) copyArrayGrow1(array, Boolean.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static byte[] add(byte[] array, byte element) {
		byte[] newArray = (byte[]) copyArrayGrow1(array, Byte.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, '0')       = ['0']
	 * ArrayUtils.add(['1'], '0')      = ['1', '0']
	 * ArrayUtils.add(['1', '0'], '1') = ['1', '0', '1']
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static char[] add(char[] array, char element) {
		char[] newArray = (char[]) copyArrayGrow1(array, Character.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static double[] add(double[] array, double element) {
		double[] newArray = (double[]) copyArrayGrow1(array, Double.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static float[] add(float[] array, float element) {
		float[] newArray = (float[]) copyArrayGrow1(array, Float.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static int[] add(int[] array, int element) {
		int[] newArray = (int[]) copyArrayGrow1(array, Integer.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static long[] add(long[] array, long element) {
		long[] newArray = (long[]) copyArrayGrow1(array, Long.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 *
	 * @param array
	 *            the array to copy and add the element to, may be {@code null}
	 * @param element
	 *            the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static short[] add(short[] array, short element) {
		short[] newArray = (short[]) copyArrayGrow1(array, Short.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Returns a copy of the given array of size 1 greater than the argument. The last value of the array is left to the default value.
	 *
	 * @param array
	 *            The array to copy, must not be {@code null}.
	 * @param newArrayComponentType
	 *            If {@code array} is {@code null}, create a size 1 array of this type.
	 * @return A new copy of the array of size 1 greater than the input.
	 */
	private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
		if (array != null) {
			int arrayLength = Array.getLength(array);
			Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return Array.newInstance(newArrayComponentType, 1);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, null)      = [null]
	 * ArrayUtils.add(null, 0, "a")       = ["a"]
	 * ArrayUtils.add(["a"], 1, null)     = ["a", null]
	 * ArrayUtils.add(["a"], 1, "b")      = ["a", "b"]
	 * ArrayUtils.add(["a", "b"], 3, "c") = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param <T>
	 *            the component type of the array
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 * @throws IllegalArgumentException
	 *             if both array and element are null
	 */
	public static <T> T[] add(T[] array, int index, T element) {
		Class<?> clss = null;
		if (array != null) {
			clss = array.getClass().getComponentType();
		} else if (element != null) {
			clss = element.getClass();
		} else {
			throw new IllegalArgumentException("Array and element cannot both be null");
		}
		@SuppressWarnings("unchecked")
		// the add method creates an array of type clss, which is type T
		final T[] newArray = (T[]) add(array, index, element, clss);
		return newArray;
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, true)          = [true]
	 * ArrayUtils.add([true], 0, false)       = [false, true]
	 * ArrayUtils.add([false], 1, true)       = [false, true]
	 * ArrayUtils.add([true, false], 1, true) = [true, true, false]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static boolean[] add(boolean[] array, int index, boolean element) {
		return (boolean[]) add(array, index, Boolean.valueOf(element), Boolean.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add(null, 0, 'a')            = ['a']
	 * ArrayUtils.add(['a'], 0, 'b')           = ['b', 'a']
	 * ArrayUtils.add(['a', 'b'], 0, 'c')      = ['c', 'a', 'b']
	 * ArrayUtils.add(['a', 'b'], 1, 'k')      = ['a', 'k', 'b']
	 * ArrayUtils.add(['a', 'b', 'c'], 1, 't') = ['a', 't', 'b', 'c']
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static char[] add(char[] array, int index, char element) {
		return (char[]) add(array, index, Character.valueOf(element), Character.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add([1], 0, 2)         = [2, 1]
	 * ArrayUtils.add([2, 6], 2, 3)      = [2, 6, 3]
	 * ArrayUtils.add([2, 6], 0, 1)      = [1, 2, 6]
	 * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static byte[] add(byte[] array, int index, byte element) {
		return (byte[]) add(array, index, Byte.valueOf(element), Byte.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add([1], 0, 2)         = [2, 1]
	 * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
	 * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
	 * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static short[] add(short[] array, int index, short element) {
		return (short[]) add(array, index, Short.valueOf(element), Short.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add([1], 0, 2)         = [2, 1]
	 * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
	 * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
	 * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static int[] add(int[] array, int index, int element) {
		return (int[]) add(array, index, Integer.valueOf(element), Integer.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add([1L], 0, 2L)           = [2L, 1L]
	 * ArrayUtils.add([2L, 6L], 2, 10L)      = [2L, 6L, 10L]
	 * ArrayUtils.add([2L, 6L], 0, -4L)      = [-4L, 2L, 6L]
	 * ArrayUtils.add([2L, 6L, 3L], 2, 1L)   = [2L, 6L, 1L, 3L]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static long[] add(long[] array, int index, long element) {
		return (long[]) add(array, index, Long.valueOf(element), Long.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add([1.1f], 0, 2.2f)               = [2.2f, 1.1f]
	 * ArrayUtils.add([2.3f, 6.4f], 2, 10.5f)        = [2.3f, 6.4f, 10.5f]
	 * ArrayUtils.add([2.6f, 6.7f], 0, -4.8f)        = [-4.8f, 2.6f, 6.7f]
	 * ArrayUtils.add([2.9f, 6.0f, 0.3f], 2, 1.0f)   = [2.9f, 6.0f, 1.0f, 0.3f]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static float[] add(float[] array, int index, float element) {
		return (float[]) add(array, index, Float.valueOf(element), Float.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given element on the specified position. The component type of the returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type is the same as the element.
	 * </p>
	 *
	 * <pre>
	 * ArrayUtils.add([1.1], 0, 2.2)              = [2.2, 1.1]
	 * ArrayUtils.add([2.3, 6.4], 2, 10.5)        = [2.3, 6.4, 10.5]
	 * ArrayUtils.add([2.6, 6.7], 0, -4.8)        = [-4.8, 2.6, 6.7]
	 * ArrayUtils.add([2.9, 6.0, 0.3], 2, 1.0)    = [2.9, 6.0, 1.0, 0.3]
	 * </pre>
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index > array.length).
	 */
	public static double[] add(double[] array, int index, double element) {
		return (double[]) add(array, index, Double.valueOf(element), Double.TYPE);
	}

	/**
	 * Underlying implementation of add(array, index, element) methods. The last parameter is the class, which may not equal element.getClass for primitives.
	 *
	 * @param array
	 *            the array to add the element to, may be {@code null}
	 * @param index
	 *            the position of the new object
	 * @param element
	 *            the object to add
	 * @param clss
	 *            the type of the element being added
	 * @return A new array containing the existing elements and the new element
	 */
	private static Object add(Object array, int index, Object element, Class<?> clss) {
		if (array == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
			}
			Object joinedArray = Array.newInstance(clss, 1);
			Array.set(joinedArray, 0, element);
			return joinedArray;
		}
		int length = Array.getLength(array);
		if (index > length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}
		Object result = Array.newInstance(clss, length + 1);
		System.arraycopy(array, 0, result, 0, index);
		Array.set(result, index, element);
		if (index < length) {
			System.arraycopy(array, index, result, index + 1, length - index);
		}
		return result;
	}

	// Clone
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Shallow clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * The objects in the array are not cloned, thus there is no special handling for multi-dimensional arrays.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param <T>
	 *            the component type of the array
	 * @param array
	 *            the array to shallow clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static long[] clone(long[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static int[] clone(int[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static short[] clone(short[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static char[] clone(char[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static double[] clone(double[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static float[] clone(float[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 *
	 * @param array
	 *            the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static boolean[] clone(boolean[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

}
