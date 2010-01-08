package com.googlecode.deadalus.geoutils;

import java.util.HashMap;
import java.util.Map;

public final class GeoHash {
	private static final long FIRST_BIT_FLAGGED = 0x8000000000000000l;
	private static final char[] base32 = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
			'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };


	//Taken from Lucene contrib spatial
	private final static Map<Character, Integer> _decodemap = new HashMap<Character, Integer>();
	static {
		int sz = base32.length;
		for (int i = 0; i < sz; i++ ){
			_decodemap.put(base32[i], i);
		}
	}

	private final long bits;
    private final byte significantBits;
	private final Point point;
	private final Point[] boundingBox;
    private final String hash;

	private GeoHash(double latitude, double longitude, int desiredPrecision) {
        long bits = 0;
        byte significantBits = 0;
		point = new Point(latitude, longitude);
		if (desiredPrecision > 64) desiredPrecision = 64;

		boolean isEvenBit = true;
		double[] latitudeRange = { -90, 90 };
		double[] longitudeRange = { -180, 180 };
		double mid;

		while (significantBits < desiredPrecision) {
			if (isEvenBit) {
				mid = (longitudeRange[0] + longitudeRange[1]) / 2;
				if (longitude > mid) {
					significantBits++;
		            bits <<= 1;
		            bits = bits | 0x1;
					longitudeRange[0] = mid;
				} else {
					significantBits++;
		            bits <<= 1;
					longitudeRange[1] = mid;
				}
			} else {
				mid = (latitudeRange[0] + latitudeRange[1]) / 2;
				if (latitude > mid) {
					significantBits++;
		            bits <<= 1;
		            bits = bits | 0x1;
					latitudeRange[0] = mid;
				} else {
					significantBits++;
		            bits <<= 1;
					latitudeRange[1] = mid;
				}
			}
			isEvenBit = !isEvenBit;
		}

		boundingBox = new Point[] {
				new Point(latitudeRange[0], longitudeRange[0]),
				new Point(latitudeRange[1], longitudeRange[1]) };

		bits <<= (64 - desiredPrecision);
        this.bits = bits;
        this.significantBits = significantBits;
        this.hash = toBase32(bits,significantBits);
	}

	/**
	 * This method uses the given number of characters as the desired precision
	 * value. The hash can only be 64bits long, thus a maximum precision of 12
	 * characters can be achieved.
	 */
	public static GeoHash withCharacterPrecision(double latitude,double longitude, int numberOfCharacters) {
		int desiredPrecision = (numberOfCharacters * 5 <= 60) ? numberOfCharacters * 5 : 60;
		return new GeoHash(latitude, longitude, desiredPrecision);
	}

	public static GeoHash fromGeohashString(String geohash) {
		final double[] points = decode(geohash);
		return GeoHash.withCharacterPrecision(points[0], points[1], geohash.length());
	}
	
	//Adapted from Lucene contrib's spatial
	private static double[] decode (String geohash){
		double[] lat_interval = {-90.0 , 90.0};
		double[] lon_interval = {-180.0, 180.0};
		
		double lat_err =  90.0;
		double lon_err = 180.0;
		boolean is_even = true;
		int sz = geohash.length();
		int[] bits = {16, 8, 4, 2, 1};
		int bsz = bits.length;
		double latitude, longitude;
		for (int i = 0; i < sz; i++){
			
			int cd = _decodemap.get(geohash.charAt(i));
			
			for (int z = 0; z< bsz; z++){
				int mask = bits[z];
				if (is_even){
					lon_err /= 2;
					if ((cd & mask) != 0){
						lon_interval[0] = (lon_interval[0]+lon_interval[1])/2;
					} else {
						lon_interval[1] = (lon_interval[0]+lon_interval[1])/2;
					}
					
				} else {
					lat_err /=2;
				
					if ( (cd & mask) != 0){
						lat_interval[0] = (lat_interval[0]+lat_interval[1])/2;
					} else {
						lat_interval[1] = (lat_interval[0]+lat_interval[1])/2;
					}
				}
				is_even = !is_even;
			}
		
		}
		latitude  = (lat_interval[0] + lat_interval[1]) / 2;
		longitude = (lon_interval[0] + lon_interval[1]) / 2;

		return new double []{latitude, longitude, lat_err, lon_err};
	}

    /**
     *
     * @return  the precision of this GeoHash (between 0 and 12)
     */
    public final int getPrecision() {
        return ((int)significantBits) / 5;     
    }

    public final String getHash() {
        return hash;
    }

    /**
	 * returns the {@link Point} that was originally used to set up this
	 * {@link GeoHash}
	 */
	public final Point getPoint() {
		return point;
	}

    /**
	 * returns true if this is within the given geohash bounding box.
	 */
	public final boolean within(GeoHash boundingBox) {
		return (bits & boundingBox.mask()) == boundingBox.bits;
	}

    /**
	 * get the base32 string for this geohash.
	 */
	private static String toBase32(long bits,long significantBits) {
		StringBuilder buf = new StringBuilder();

		long firstFiveBitsMask = 0xf800000000000000l;
		long bitsCopy = bits;
		int partialChunks = (int) Math.ceil(((double) significantBits / 5));

		for (int i = 0; i < partialChunks; i++) {
			int pointer = (int) ((bitsCopy & firstFiveBitsMask) >>> 59);
			buf.append(base32[pointer]);
			bitsCopy <<= 5;
		}
		return buf.toString();
	}

	/**
	 * return the center of this {@link GeoHash}s bounding box. this is rarely
	 * the same point that was used to build the hash.
	 */
	// TODO: make sure this method works as intented for corner cases!
	public final Point getBoundingBoxCenterPoint() {
		double centerLatitude = (boundingBox[0].getLatitude() + boundingBox[1].getLatitude()) / 2;
		double centerLongitude = (boundingBox[0].getLongitude() + boundingBox[1].getLongitude()) / 2;
		return new Point(centerLatitude, centerLongitude);
	}

	/**
	 * @return an array containing the two points: upper left, lower right of
	 *         the bounding box.
	 */
	public final Point[] getBoundingBoxPoints() {
		return boundingBox;
	}

	@Override
	public final String toString() {
		return hash;
	}

	/**
	 * return a long mask for this hashes significant bits.
	 */
	private long mask() {
		if (significantBits == 0) {
			return 0;
		} else {
			long value = FIRST_BIT_FLAGGED;
			value >>= (significantBits - 1);
			return value;
		}
	}

	public final GeoHash getNorthernNeighbour() {
		long[] latitudeBits = getRightAlignedLatitudeBits();
		long[] longitudeBits = getRightAlignedLongitudeBits();
		latitudeBits[0] -= 1;
		latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
		return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
	}
	
	public final GeoHash getSouthernNeighbour(){
		long[] latBits = getRightAlignedLatitudeBits();
		long[] lonBits = getRightAlignedLongitudeBits();
		latBits[0] += 1;
		latBits[0] = maskLastNBits(latBits[0], latBits[1]);
		return recombineLatLonBitsToHash(latBits, lonBits);
	}
	
	public final GeoHash getEasternNeighbour(){
		long[] latBits = getRightAlignedLatitudeBits();
		long[] lonBits = getRightAlignedLongitudeBits();
		lonBits[0] += 1;
		lonBits[0] = maskLastNBits(lonBits[0], lonBits[1]);
		return recombineLatLonBitsToHash(latBits, lonBits);
	}
	
	public final GeoHash getWesternNeighbour(){
		long[] latBits = getRightAlignedLatitudeBits();
		long[] lonBits = getRightAlignedLongitudeBits();
		lonBits[0] -= 1;
		lonBits[0] = maskLastNBits(lonBits[0], lonBits[1]);
		return recombineLatLonBitsToHash(latBits, lonBits);
	}

	private static GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
		long bits = 0;
        byte significantBits = 0;
		boolean isEvenBit = false;
		latBits[0] <<= (64 - latBits[1]);
		lonBits[0] <<= (64 - lonBits[1]);
		for (int i = 0; i < latBits[1] + lonBits[1]; i++) {
			if (isEvenBit) {
				if ((latBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
					significantBits++;
		            bits <<= 1;
		            bits = bits | 0x1;
				} else {
					significantBits++;
		            bits <<= 1;
				}
				latBits[0] <<= 1;
			} else {
				if ((lonBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
					significantBits++;
		            bits <<= 1;
		            bits = bits | 0x1;
				} else {
					significantBits++;
		            bits <<= 1;
				}
				lonBits[0] <<= 1;
			}
			isEvenBit = !isEvenBit;
		}
		bits <<= (64 - significantBits);
		return fromGeohashString(toBase32(bits,significantBits));
	}

	private static long maskLastNBits(long value, long n) {
		long mask = 0xffffffffffffffffl;
		mask >>>= (64 - n);
		return value & mask;
	}

	private long[] getRightAlignedLatitudeBits() {
		long value = 0;
		long copyOfBits = bits;
		copyOfBits <<= 1;
		int numberOfBits = getNumberOfLatLonBits()[0];
		for (int i = 0; i < numberOfBits; i++) {
			if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
				value |= 0x1;
			}
			value <<= 1;
			copyOfBits <<= 2;
		}
		value >>>= 1;
		return new long[] { value, numberOfBits };
	}

	private long[] getRightAlignedLongitudeBits() {
		long value = 0;
		long copyOfBits = bits;
		int numberOfBits = getNumberOfLatLonBits()[1];
		for (int i = 0; i < numberOfBits; i++) {
			if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
				value |= 0x1;
			}
			value <<= 1;
			copyOfBits <<= 2;
		}
		value >>>= 1;
		return new long[] { value, numberOfBits };
	}

	private int[] getNumberOfLatLonBits() {
		if (significantBits % 2 == 0) {
			return new int[] { significantBits / 2, significantBits / 2 };
		} else {
			return new int[] { significantBits / 2, significantBits / 2 + 1 };
		}
	}

    public static final class Point {
        private final double longitude;
	    private final double latitude;

        public Point(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public final double getLongitude() {
            return longitude;
        }

        public final double getLatitude() {
            return latitude;
        }

        @Override
        public final String toString() {
            return String.format("(%f,%f)", latitude, longitude);
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            return Double.compare(point.latitude, latitude) == 0 && Double.compare(point.longitude, longitude) == 0;

        }

        @Override
        public final int hashCode() {
            int result;
            long temp;
            temp = longitude != +0.0d ? Double.doubleToLongBits(longitude) : 0L;
            result = (int) (temp ^ (temp >>> 32));
            temp = latitude != +0.0d ? Double.doubleToLongBits(latitude) : 0L;
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
