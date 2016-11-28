package com.g3.seapp.server;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The implementation of the CountryService.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities 
 * 	Defines an endpoint for weatherdata.
 */
@SuppressWarnings("serial")
public class CountryServiceImpl extends RemoteServiceServlet implements CountryService {
	
	/**
	 * Sorts the given measurements list
	 * 
	 * @param measurements The measurements list to sort
	 * @param col The column description to sort for
	 * @param desc Is descending sort (false = ascending)
	 * @pre measurements != null
	 * @post measurements is sorted for the given column
	 * 
	 * @return nothing
	 */
	private void sortMeasurements(ArrayList<Measurement> measurements, Measurement.MeasurementType col, final boolean desc) {
		switch(col) {
		case DATE:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			        int comp = s1.getDate().compareTo(s2.getDate());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case COUNTRY:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			        int comp = s1.getCountry().compareTo(s2.getCountry());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case CITY:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = s1.getCity().compareTo(s2.getCity());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case AVG:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getAvg(), s2.getAvg());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case ERROR:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getError(), s2.getError());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case LAT:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getCoords().getLat(), s2.getCoords().getLat());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case LON:
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getCoords().getLon(), s2.getCoords().getLon());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		}
	}

	/**
	 * Filters the measurement dataset
	 *
	 * @param measurements The measurements dataset (ArrayList) to filter
	 * @param filters A HashMap with the filters
	 * @return The filtered measurement dataset
	 */
	private ArrayList<Measurement> filterMeasurements(ArrayList<Measurement> measurements, HashMap<Measurement.MeasurementType, String> filters) {
		ArrayList<Measurement> newMeasurements = new ArrayList<Measurement>();
		
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i = 0; i < measurements.size(); i++) {
			int okCount = 0;
			for (Measurement.MeasurementType filter : filters.keySet()) {
				try {
					switch (filter) {
						case DATE:
							if (measurements.get(i).getDate().compareTo(dateParser.parse(filters.get(filter))) == 0)
								okCount++;
							break;
						case COUNTRY:
							if (measurements.get(i).getCountry().toLowerCase().contains(filters.get(filter).toLowerCase()))
								okCount++;
							break;
						case CITY:
							if (measurements.get(i).getCity().toLowerCase().contains(filters.get(filter).toLowerCase()))
								okCount++;
							break;
						case AVG:
							if (Float.compare(measurements.get(i).getAvg(), Float.parseFloat(filters.get(filter))) == 0)
								okCount++;
							break;
						case ERROR:
							String filterString = filters.get(filter);
							if(filterString.startsWith("<")) {
								float floatFilter = Float.parseFloat(filterString.substring(1));
								if(measurements.get(i).getError() <= floatFilter)
									okCount++;
							}
							else if (Float.compare(measurements.get(i).getError(), Float.parseFloat(filters.get(filter))) == 0)
								okCount++;
							break;
						case LAT:
							if (Float.compare(measurements.get(i).getCoords().getLat(), Float.parseFloat(filters.get(filter))) == 0)
								okCount++;
							break;
						case LON:
							if (Float.compare(measurements.get(i).getCoords().getLon(), Float.parseFloat(filters.get(filter))) == 0)
								okCount++;
							break;
					}
				} catch (Exception ex) {
					System.out.println("Was not able to parse input: '" + filters.get(filter) + "'.");
					return new ArrayList<>();
				}
			}
			if(okCount == filters.keySet().size()) newMeasurements.add(measurements.get(i));
		}

		return newMeasurements;
	}

	/**
	 * Returns all the measurements from start to end,
	 * sorted for sortCol.
	 * 
	 * @param start Start of the measurement data range
	 * @param end End of the measurement data range
	 * @param sortCol The column descriptor for which the data should be sorted for
	 * @param desc Boolean - True if sort should be descending, false otherwise
	 * @pre end > start
	 * @post DataManager.getData() is sorted for sortCol
	 * 
	 * @return An array of measurements
	 */
	@Override
	public ArrayList<Measurement> getMeasurements(int start, int end, Measurement.MeasurementType sortCol,
												  boolean asc, HashMap<Measurement.MeasurementType, String> filters) {
		ArrayList<Measurement> measurements = DataManager.getMeasurements();
		
		if(measurements == null) return new ArrayList<Measurement>();
		
		if(sortCol != null)
			sortMeasurements(measurements, sortCol, asc);
		
		if(filters != null && !filters.isEmpty())
			measurements = filterMeasurements(measurements, filters);

		if(measurements.size() - 1 < start) return new ArrayList<>();
		if(measurements.size() - 1 < end) return new ArrayList<>(measurements.subList(start, measurements.size()));

		// Gotta get rid of some data structures here
		//System.gc();
		return new ArrayList<>(measurements.subList(start, end));
	}

	/**
	 * Gets all possible values for a specific measurement type (Used for autocompletion)
	 *
	 * @param type The measurement type to get the values for
	 * @return A list of all possible values for the specified measurement type
	 */
	@Override
	public ArrayList<String> getNames(Measurement.MeasurementType type) {
		Set<String> names = new HashSet<>();
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

		switch(type) {
			case COUNTRY:
				for(Measurement country : DataManager.getMeasurements())
					names.add(country.getCountry());
				break;
			case CITY:
				for(Measurement country : DataManager.getMeasurements())
					names.add(country.getCity());
				break;
			case DATE:
				for(Measurement measurement : DataManager.getMeasurements()) {
					String dateString = dateParser.format(measurement.getDate());
					names.add(dateString);
				}
				break;
			case AVG:
				for(Measurement measurement : DataManager.getMeasurements()) {
					String avgString = Float.toString(measurement.getAvg());
					names.add(avgString);
				}
				break;
			case ERROR:
				for(Measurement measurement : DataManager.getMeasurements()) {
					String errorString = Float.toString(measurement.getError());
					names.add(errorString);
				}
				break;
			case LAT:
				for(Measurement measurement : DataManager.getMeasurements()) {
					String latString = Float.toString(measurement.getCoords().getLat());
					names.add(latString);
				}
				break;
			case LON:
				for(Measurement measurement : DataManager.getMeasurements()) {
					String lonString = Float.toString(measurement.getCoords().getLon());
					names.add(lonString);
				}
				break;
		}

		return new ArrayList<>(names);
	}

	/**
	 * Gets the number of measurement entries after filtering the the dataset
	 *
	 * @param filters The filters to apply to the data
	 * @return The number of measurement entries after filtering the dataset
	 */
	@Override
	public Integer getMeasurementEntrySize(HashMap<Measurement.MeasurementType, String> filters) {
		if(filters != null && !filters.isEmpty())
			return filterMeasurements(DataManager.getMeasurements(), filters).size();
		return DataManager.getMeasurements().size();
	}

	@Override
	public HashMap<String, Float> getAverageTempOfYear(int year) {

		HashMap<String, Float> avTemp = new HashMap<>();
		HashMap<String, Integer> numberOfMeasures = new HashMap<>();

		for (Measurement meas : DataManager.getMeasurements()) {
			if (meas.getDate().getYear() == year) continue;
			String country = meas.getCountry();
			float sum;

			if (!avTemp.containsKey(country))
				sum = meas.getAvg();
			else
				sum = avTemp.get(country) + meas.getAvg();

			avTemp.put(country, sum);

			int nrOfMeasPerCountry;

			if (!numberOfMeasures.containsKey(country))
				nrOfMeasPerCountry = 1;
			else
				nrOfMeasPerCountry = numberOfMeasures.get(country) + 1;

			numberOfMeasures.put(country, nrOfMeasPerCountry);
		}

		for (String country : avTemp.keySet()) {
			float tempSum = avTemp.get(country);
			float nrOfMeas = numberOfMeasures.get(country);
			float averageTemp = tempSum / nrOfMeas;
			avTemp.put(country, averageTemp);
		}

		return avTemp;

	}

	/**
	 * Gets an aggregation of the specified Measurement and AggregationType.
	 *
	 * @param measType The MeasurementType to specify the type of data to compute
	 * @param aggType The AggregationType to specify the type of aggregation to compute
	 * @return The specified aggregation of the specified data
	 */
	@Override
	public Float getAggregation(Measurement.MeasurementType measType, Measurement.AggregationType aggType) {
		ArrayList<Measurement> measurements = DataManager.getMeasurements();
		ArrayList<Float> measurementData = new ArrayList<>();

		for(Measurement meas : measurements) {
			switch(measType) {
				case AVG: measurementData.add(meas.getAvg()); break;
				case ERROR: measurementData.add(meas.getError()); break;
				case LAT: measurementData.add(meas.getCoords().getLat()); break;
				case LON: measurementData.add(meas.getCoords().getLon()); break;
				default: return null;
			}
		}

		Collections.sort(measurementData, new Comparator<Float>() {
			public int compare(Float s1, Float s2) {
				return Float.compare(s1, s2);
			}
		});

		float result = 0;

		switch(aggType) {
			case AVG:
				for(Float f : measurementData)
					result += f;
				return result / measurementData.size();
			case MAX:
				result = Float.MIN_VALUE;
				for(Float f : measurementData)
					if(result < f)
						result = f;
				return result;
			case MIN:
				result = Float.MAX_VALUE;
				for(Float f : measurementData)
					if(result > f)
						result = f;
				return result;
			case MEDIAN:
				int idx = measurementData.size() / 2;
				if(measurementData.size() % 2 == 0)
					return (measurementData.get(idx) + measurementData.get(idx+1)) / 2;
				else return measurementData.get(idx);
			default:
				return null;
		}
	}
}
