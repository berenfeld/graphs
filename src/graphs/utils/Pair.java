/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.utils;

import java.util.Objects;

public class Pair<T> implements Comparable {
    
    public T first;
    public T second;

    @Override
    public int compareTo(Object o) {
        Pair<T> other = (Pair<T>) o;
        if (other == null) {
            return 1;
        }
        int c1 = ((Comparable) first).compareTo(other.first);
        if (c1 != 0) {
            return c1;
        }
        int c2 = ((Comparable) second).compareTo(other.second);
        return c2;
    }

    @Override
    public String toString() {
        return first + "," + second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.first);
        hash = 37 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof Pair)) {
            return false;
        }
        Pair<T> other = (Pair<T>)obj;
        return (other.first.equals(first)) && (other.second.equals(second));        
    }
    
}
