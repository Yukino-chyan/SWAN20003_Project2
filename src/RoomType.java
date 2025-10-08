public enum RoomType { PREP, A, B, END ;
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
