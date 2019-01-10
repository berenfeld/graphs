/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.utils.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author me
 */
public class BaseElement implements Serializable {

    // attribtes
    protected Map<String, Object> _attributes = new HashMap<>();

    private final List<AttributesListener> _attributeListeners = new ArrayList<>();

    public final void addAttributeListener(AttributesListener listener) {
        if (!_attributeListeners.contains(listener)) {
            _attributeListeners.add(listener);
        }
    }

    public void removeAttributeListener(AttributesListener listener) {
        if (_attributeListeners.contains(listener)) {
            _attributeListeners.remove(listener);
        }
    }

    public Set<String> attributeNames() {
        return _attributes.keySet();
    }

    public boolean hasAttribute(String name) {
        return _attributes.containsKey(name);
    }

    public Object getAttribute(String name) {
        return getAttribute(name, null);
    }

    public Object getAttribute(String name, Object defautValue) {
        if (!_attributes.containsKey(name)) {
            return defautValue;
        }
        return _attributes.get(name);
    }

    public final void setAttribute(String name, Object value) {
        if ( value != null) {
            if (! (value instanceof Serializable)) {
                Utils.warning("attribute " + name + " type " + value.getClass().getSimpleName());
            }
        }
        _attributes.put(name, value);

        for (AttributesListener listener : _attributeListeners) {
            listener.attributeValueChanged(name, value);
        }       
    }

    public void delAttribute(String name) {
        if ( ! _attributes.containsKey(name)) 
        {
            return;
        }
        _attributes.remove(name);
        for (AttributesListener listener : _attributeListeners) {
            listener.attributeRemoved(name);
        }                  
    }

    public void copyAttributesTo(BaseElement other) {
        _attributes.putAll(other._attributes);
    }
}
