package comp3601;

public class Record { 
    private String name;
    private String value;
    private String id;      // cloudant _id
    private String rev;     // cloudant _rev

    public Record() { 
        name = "";
        value = "";
        id = "";
        rev = "";
    } 

    public Record(String name, String value) { 
        this.name = name;
        this.value = value;
        id = "";
        rev = "";
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
}
