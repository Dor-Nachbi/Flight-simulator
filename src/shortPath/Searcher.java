package shortPath;

public interface Searcher {
    Backtrace search(Searchable<State> searchable);
}