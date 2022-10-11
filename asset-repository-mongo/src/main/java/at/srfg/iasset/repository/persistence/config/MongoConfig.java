package at.srfg.iasset.repository.persistence.config;


public class MongoConfig {

    private String url = "mongodb://root:example@localhost:27017/";
    private String name = "asset";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MongoConfig{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

