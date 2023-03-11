package ru.ptkom.provisor.models.sipUsers;



import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "in")
public class In{


    @XmlAttribute(name = "noNamespaceSchemaLocation", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    private String schemaLocation = "sip_user_show.xsd";


    @XmlElement(name = "sip")
    public List<Sip> sip;


    public void setSip(List<Sip> sip) {
        this.sip = sip;
    }


    public static class Sip {

            @XmlAttribute
            public String group;

            @XmlAttribute
            private String id;

            @XmlAttribute
            private String domain;

            @XmlAttribute
            private boolean complete;

            @XmlAttribute
            private boolean auth;


            public void setGroup(String group) {
                this.group = group;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public void setComplete(boolean complete) {
                this.complete = complete;
            }

            public void setAuth(boolean auth) {
                this.auth = auth;
            }
        }
}


