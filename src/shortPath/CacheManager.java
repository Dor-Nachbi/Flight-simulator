package shortPath;

public interface CacheManager {
    Boolean check(String problem);
    void save(String problem,String solution);
    String load(String problem);
}