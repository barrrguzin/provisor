package ru.ptkom.provisor.models.aliases;





import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "out")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutAliases {

    @XmlElementWrapper(name = "result")
    @XmlElement(name = "alias")
    private List<Alias> result;

    public List<Alias> getAliases() {
        return result;
    }


    @XmlAccessorType(XmlAccessType.NONE)
    public static class Alias {

        @XmlAttribute(name = "domain")
        private String domain;
        @XmlAttribute(name = "iface")
        private String iface;
        @XmlAttribute(name = "address")
        private String address;
        @XmlAttribute(name = "active")
        private boolean active;
        @XmlAttribute(name = "binded")
        private boolean binded;
        @XmlAttribute(name = "display_name")
        private String display_name;
        @XmlAttribute(name = "regime")
        private String regime;
        @XmlAttribute(name = "interface_name")
        private String interface_name;
        @XmlAttribute(name = "interface_type")
        private String interface_type;
        @XmlAttribute(name = "interface_owner")
        private String interface_owner;
        @XmlAttribute(name = "interface_group")
        private String interface_group;

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getIface() {
            return iface;
        }

        public void setIface(String iface) {
            this.iface = iface;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isBinded() {
            return binded;
        }

        public void setBinded(boolean binded) {
            this.binded = binded;
        }


        public String getRegime() {
            return regime;
        }

        public void setRegime(String regime) {
            this.regime = regime;
        }

        public String getInterface_name() {
            return interface_name;
        }

        public void setInterface_name(String interface_name) {
            this.interface_name = interface_name;
        }

        public String getInterface_type() {
            return interface_type;
        }

        public void setInterface_type(String interface_type) {
            this.interface_type = interface_type;
        }

        public String getInterface_owner() {
            return interface_owner;
        }

        public void setInterface_owner(String interface_owner) {
            this.interface_owner = interface_owner;
        }

        public String getInterface_group() {
            return interface_group;
        }

        public void setInterface_group(String interface_group) {
            this.interface_group = interface_group;
        }
    }}