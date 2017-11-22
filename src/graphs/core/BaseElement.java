/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author me
 */
public class BaseElement {
    // attribtes
    protected Map<String, Object> _attributes = new HashMap<String, Object>();
    
    public Object getAttribute(String name) {        
        return getAttribute(name, null);
    }
    
    public Object getAttribute(String name, Object defautValue) {
        if ( ! _attributes.containsKey(name)) {
            return defautValue;
        }
        return _attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        _attributes.put(name, value);
    }

    public void delAttribute(String name) {
        _attributes.remove(name);
    }
    
    public void copyAttributesTo(BaseElement other) {
        _attributes.putAll(other._attributes);
    }
}
