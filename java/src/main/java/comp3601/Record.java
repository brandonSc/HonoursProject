package comp3601;

import org.json.simple.JSONObject;

public class Record { 
    private String name;
    private String value;
    private String id;      // cloudant _id
    private String rev;     // cloudant _rev

    public Record() { 
        name = "";
        value = "";
        id = null;
        rev = null;
    } 

    public Record(String name, String value) { 
        this.name = name;
        this.value = value;
        id = null;
        rev = null;
    }
    
    public Record(String name, String value, String id, String rev) { 
        this.name = name;
        this.value = value;
        this.id = id;
        this.rev = rev;
    }

    public void setName(String name) { 
        this.name = name;
    }

    public String getName() { 
        return name;
    }

    public void setValue(String value) { 
        this.value = value;
    }

    public String getValue() { 
        return value;
    }

    public String getId() { 
        return id;
    }

    public void setId(String id) { 
        this.id = id;
    }

    public String getRev() { 
        return rev;
    }

    public void setRev(String rev) { 
        this.rev = rev;
    }

    public static Record parse(JSONObject o) { 
        Record r = new Record();
        r.setName((String)o.get("name"));
        r.setValue((String)o.get("value"));
        r.setId((String)o.get("_id"));
        r.setRev((String)o.get("_rev"));
        return r;
    }

    @Override
    public String toString() { 
        JSONObject o = new JSONObject();
        o.put("name", name);
        o.put("value", value);
        if ( id != null ) 
            o.put("_id", id);
        if ( rev != null ) 
            o.put("_rev", rev);
        return o.toString();
    }
}
