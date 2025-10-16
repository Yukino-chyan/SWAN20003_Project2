/**
 * RoomType enum: Enumerates all room types in the game.
 */
public enum RoomType { PREP, A, B, END ;
    /**
     * Convert a string to a corresponding RoomType.
     * @param s The room name text (e.g., "prep", "a", "b", "end"), case-insensitive and trimmed.
     * @return The matching RoomType value.
     * @throws IllegalArgumentException If {@code s} is null or does not match any known room.
     */
    public static RoomType toRoomType(String s) {
        if (s == null) throw new IllegalArgumentException("room is null");
        switch (s.trim().toLowerCase()) {
            case "prep" -> { return RoomType.PREP; }
            case "a"    -> { return RoomType.A; }
            case "b"    -> { return RoomType.B; }
            case "end"  -> { return RoomType.END; }
            default     -> throw new IllegalArgumentException("未知房间: " + s);
        }
    }
}
