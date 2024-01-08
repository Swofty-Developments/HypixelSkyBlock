package net.swofty.types.generic.utility;

import java.util.*;

/**
 * A page-based ArrayList
 * * Pages are NOT index-based (they start at 1, not 0)
 * * A page sublist is created when you call a method to get one; it is not created and then updated accordingly.
 *
 * @param <T> Type of the ArrayList
 */
public class PaginationList<T> extends ArrayList<T> {
    private int elementsPerPage;

    public PaginationList(Collection<T> collection, int elementsPerPage) {
        super(collection);
        this.elementsPerPage = elementsPerPage;
    }

    /**
     * Defines a new Pagination List.
     *
     * @param elementsPerPage How many elements will be included on a page of the list.
     */
    public PaginationList(int elementsPerPage) {
        super();
        this.elementsPerPage = elementsPerPage;
    }

    /**
     * Defines a new Pagination List.
     *
     * @param elementsPerPage How many elements will be included on a page of the list.
     * @param elements        Elements to add to the list immediately.
     */
    public PaginationList(int elementsPerPage, T... elements) {
        super(Arrays.asList(elements));
        this.elementsPerPage = elementsPerPage;
    }

    /**
     * @return The amount of elements per page.
     */
    public int getElementsPerPage() {
        return elementsPerPage;
    }

    /**
     * Sets the element count per page.
     *
     * @param elementsPerPage Updated element count per page
     */
    public void setElementsPerPage(int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }

    /**
     * @return The number of pages this list holds.
     */
    public int getPageCount() {
        return (int) Math.ceil((double) size() / (double) elementsPerPage);
    }

    /**
     * Get a page from the list.
     *
     * @param page Page you want to access.
     * @return A sublist of only the elements from that page.
     */
    public List<T> getPage(int page) {
        if (page < 1 || page > getPageCount()) return null;
        int startIndex = (page - 1) * elementsPerPage;
        int endIndex = Math.min(startIndex + elementsPerPage, this.size());
        return subList(startIndex, endIndex);
    }

    /**
     * @return A 2D List with every page.
     */
    public List<List<T>> getPages() {
        List<List<T>> pages = new ArrayList<>();
        for (int i = 1; i <= getPageCount(); i++)
            pages.add(getPage(i));
        return pages;
    }

    public PaginationList<T> addAll(T[] t) {
        Collections.addAll(this, t);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 1; i <= getPageCount(); i++) {
            res.append("Page ").append(i).append(": ").append("\n");
            for (T element : getPage(i))
                res.append(" - ").append(element).append("\n");
        }
        return res.toString();
    }
}
