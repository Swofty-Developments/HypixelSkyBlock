package net.swofty.data.datapoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.swofty.data.Datapoint;
import net.swofty.serializer.Serializer;

import java.util.ArrayList;
import java.util.List;

public class DatapointIntegerList extends Datapoint<List<Integer>> {
	
	public DatapointIntegerList(String key, List<Integer> value) {
		super(key, value, new Serializer<List<Integer>>() {
			@Override
			public String serialize(List<Integer> value) throws JsonProcessingException {
				ArrayList<String> list = new ArrayList<>(value.size());
				for(Integer i : value)
					list.add(String.valueOf(i));
				return String.join(",", list);
			}
			
			@Override
			public List<Integer> deserialize(String json) throws JsonProcessingException {
				String[] split = json.split(",");
				ArrayList<Integer> list = new ArrayList<>(split.length);
				for(String s : split)
					list.add(Integer.parseInt(s));
				return list;
			}
		});
	}
	
	public DatapointIntegerList(String key) {
		this(key, new ArrayList<>());
	}
	
	public void add(Integer value) {
		List<Integer> current = getValue();
		current.add(value);
		setValue(current);
	}
	
	public void remove(Integer value) {
		List<Integer> current = getValue();
		current.remove((Object) value);
		setValue(current);
	}
	
	public boolean has(Integer value) {
		return getValue().contains(value);
	}
	
	/**
	 * @param value
	 * @return true if it was added, false if it wasn't
	 */
	public boolean hasOrAdd(Integer value) {
		if(has(value))
			return false;
		add(value);
		return true;
	}
	
}
