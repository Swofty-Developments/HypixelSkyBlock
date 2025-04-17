package net.swofty.types.generic.block.placement.states;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.swofty.types.generic.block.placement.states.error.StateError;
import net.swofty.types.generic.block.placement.states.error.UnknownBlockStatesKey;
import net.swofty.types.generic.block.placement.states.state.State;
import org.jetbrains.annotations.ApiStatus;
//import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockState {
    private final HashMap<String, String> block_states = new HashMap<>();

    private Block block;

    private BlockHandler blockHandler;
    private CompoundBinaryTag blockNbt;

    //Internal use of BlockState to create a new BlockState from a Block and save data from the Block
    protected BlockState(Block block) {
        this.blockHandler = block.handler();
        this.blockNbt = block.nbt();
        this.block = block;
        //Copy All Entry
        block_states.putAll(block.properties());
    }

    /**
     * internal set method to set a key and a value to the block states map of the block
     *
     * @param key   key of the block states
     * @param value value of the block states
     */
    @ApiStatus.Internal
    private void private_set(String key, String value) {
        if (!block_states.containsKey(key)) {
            throw new UnknownBlockStatesKey("Unknown key " + key + " for block states of " + block().name());
        } else {
            block_states.remove(key);
            block_states.put(key.toLowerCase(), value.toLowerCase());
        }
    }

    /**
     * Set a key and a value to the block states map of the block
     *
     * @param key   key of the block states
     * @param value value of the block states
     */
    public void set(State<?> key, State<?> value) {
        private_set(key.getKey(), value.getValue());
    }

    /**
     * Set a key and a value to the block states map of the block
     *
     * @param key   key of the block states
     * @param value value of the block states
     */
    public <T extends Comparable<T>> void set(State<T> key, T value) {
        private_set(key.getKey(), String.valueOf(value));
    }

    /**
     * Set a state to the block states map of the block
     *
     * @param state state of the block states
     */
    public void set(State<?> state) {
        private_set(state.getKey(), state.getValue());
    }

    /**
     * Get the block states from the state key
     *
     * @param stateKey state key
     */
    public <T extends Comparable<T>> T get(State<T> stateKey) {
        return stateKey.parse(block_states.get(stateKey.getKey()));
    }

    public <T extends State<T> & Comparable<T>> T get(Class<T> type) {
        if (type.isEnum()) {
            return get(type.getEnumConstants()[0]);
        } else {
            throw new StateError("You need to specify a key for the state " + type.getSimpleName());
        }
    }

    public <T extends State<T> & Comparable<T>> T get(State<?> key, Class<T> type) {
        if (type.isEnum()) {
            return type.getEnumConstants()[0].parse(getRaw(key.getKey()));
        } else {
            throw new StateError("You need to specify a key for the state " + type.getSimpleName());
        }
    }

    public <T extends State<T> & Comparable<T>> T getOr(Class<T> stateKey, T miss) {
        try {
            return miss.getClass().equals(stateKey) ? get(stateKey) : miss;
        } catch (Exception e) {
            return miss;
        }
    }

    public boolean has(State<?> stateKey) {
        return block_states.containsKey(stateKey.getKey());
    }

    public boolean has(Class<? extends State<?>> type) {
        if (type.isEnum()) {
            return block_states.containsKey(type.getEnumConstants()[0].getKey());
        } else {
            throw new StateError("Error, you need to specify a key for the blocksate " + type.getSimpleName());
        }
    }

    @Deprecated
    public boolean has(String key) {
        return block_states.containsKey(key);
    }

    @Deprecated
    public String getRaw(State<?> stateKey) {
        return block_states.get(stateKey.getKey());
    }

    @Deprecated
    public String getRaw(String key) {
        return block_states.get(key);
    }

    @SafeVarargs
    public final <T extends Comparable<T>> List<T> getStates(State<T>... stateKeys) {
        List<T> list = new ArrayList<>();
        for (State<T> stateKey : stateKeys)
            list.add(get(stateKey));
        return list;
    }

    public <T extends State<T> & Comparable<T>> List<T> getAllStateValue(Class<T> type) {
        if (type.isEnum()) {
            List<T> valueT = new ArrayList<>();
            T valueState = type.getEnumConstants()[0];

            for (Map.Entry<String, String> entry : block_states.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(valueState.getKey())) {
                    valueT.add(valueState.parse(entry.getValue()));
                }
            }

            return valueT;
        } else {
            throw new StateError("Error, you need to specify a key for the BlockState " + type.getSimpleName());
        }
    }

    public Block block() {
        if (blockHandler != null)
            block = block.withHandler(blockHandler);

        if (blockNbt != null)
            block = block.withNbt(blockNbt);

        return block.withProperties(map());
    }

    public HashMap<String, String> map() {
        return block_states;
    }

    public void withBlock(Block block) {
        block_states.clear();
        this.block = block;
        this.blockNbt = block.nbt();
        this.blockHandler = block.handler();
        block_states.putAll(block.properties());
    }

    @Override
    public String toString() {
        return "BlockState{" +
                "states=" + mapToString(block_states) +
                ", block=" + block.namespace() +
                '}';
    }

    private String mapToString(Map<?, ?> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
