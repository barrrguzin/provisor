package ru.ptkom.provisor.models.sipUsers;



import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "out")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlTransient
public class OutUsers {

    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }


@XmlAccessorType(XmlAccessType.NONE)
public static class User {

    @XmlAttribute(name = "active")
    private boolean active;
    @XmlAttribute(name = "alarm_enable")
    private boolean alarm_enable;
    @XmlAttribute(name = "aliases_list")
    private String aliases_list;
    @XmlAttribute(name = "auth")
    private String auth;
    @XmlAttribute(name = "auth_qop")
    private boolean auth_qop;
    @XmlAttribute(name = "auto_answer_version")
    private String auto_answer_version;
    @XmlAttribute(name = "category_to_sip")
    private String category_to_sip;
    @XmlAttribute(name = "contact")
    private String contact;
    @XmlAttribute(name = "digest")
    private boolean digest;
    @XmlAttribute(name = "display_encoding")
    private String display_encoding;
    @XmlAttribute(name = "domain")
    private String domain;
    @XmlAttribute(name = "dtmf_relay")
    private boolean dtmf_relay;
    @XmlAttribute(name = "fork_mode")
    private String fork_mode;
    @XmlAttribute(name = "group")
    private String group;
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "iface")
    private String iface;
    @XmlAttribute(name = "last_via")
    private String last_via;
    @XmlAttribute(name = "login")
    private String login;
    @XmlAttribute(name = "my_from")
    private String my_from;
    @XmlAttribute(name = "nat_traversal")
    private boolean nat_traversal;
    @XmlAttribute(name = "original_cdpn_to")
    private boolean original_cdpn_to;
    @XmlAttribute(name = "owner")
    private String owner;
    @XmlAttribute(name = "password")
    private String password;
    @XmlAttribute(name = "referred_by_as_cgpn")
    private boolean referred_by_as_cgpn;
    @XmlAttribute(name = "reg_expire_max")
    private int reg_expire_max;
    @XmlAttribute(name = "reg_expire_min")
    private int reg_expire_min;
    @XmlAttribute(name = "registered_domain_to_invite")
    private boolean registered_domain_to_invite;
    @XmlAttribute(name = "remote_ctr_indication")
    private String remote_ctr_indication;
    @XmlAttribute(name = "req100rel")
    private boolean req100rel;
    @XmlAttribute(name = "rfc4028_control")
    private String rfc4028_control;
    @XmlAttribute(name = "routing_context")
    private String routing_context;
    @XmlAttribute(name = "sip_domain")
    private String sip_domain;
    @XmlAttribute(name = "sip_modifications")
    private String sip_modifications;
    @XmlAttribute(name = "symbol_hash_as_is")
    private boolean symbol_hash_as_is;
    @XmlAttribute(name = "tel_uri_in_diversion")
    private boolean tel_uri_in_diversion;
    @XmlAttribute(name = "transit")
    private String transit;
    @XmlAttribute(name = "trusted_ip")
    private boolean trusted_ip;
    @XmlAttribute(name = "user_agent")
    private String user_agent;


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAlarm_enable() {
        return alarm_enable;
    }

    public void setAlarm_enable(boolean alarm_enable) {
        this.alarm_enable = alarm_enable;
    }

    public String getAliases_list() {
        return aliases_list;
    }

    public void setAliases_list(String aliases_list) {
        this.aliases_list = aliases_list;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isAuth_qop() {
        return auth_qop;
    }

    public void setAuth_qop(boolean auth_qop) {
        this.auth_qop = auth_qop;
    }

    public String getAuto_answer_version() {
        return auto_answer_version;
    }

    public void setAuto_answer_version(String auto_answer_version) {
        this.auto_answer_version = auto_answer_version;
    }

    public String getCategory_to_sip() {
        return category_to_sip;
    }

    public void setCategory_to_sip(String category_to_sip) {
        this.category_to_sip = category_to_sip;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isDigest() {
        return digest;
    }

    public void setDigest(boolean digest) {
        this.digest = digest;
    }

    public String getDisplay_encoding() {
        return display_encoding;
    }

    public void setDisplay_encoding(String display_encoding) {
        this.display_encoding = display_encoding;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isDtmf_relay() {
        return dtmf_relay;
    }

    public void setDtmf_relay(boolean dtmf_relay) {
        this.dtmf_relay = dtmf_relay;
    }

    public String getFork_mode() {
        return fork_mode;
    }

    public void setFork_mode(String fork_mode) {
        this.fork_mode = fork_mode;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIface() {
        return iface;
    }

    public void setIface(String iface) {
        this.iface = iface;
    }

    public String getLast_via() {
        return last_via;
    }

    public void setLast_via(String last_via) {
        this.last_via = last_via;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMy_from() {
        return my_from;
    }

    public void setMy_from(String my_from) {
        this.my_from = my_from;
    }

    public boolean isNat_traversal() {
        return nat_traversal;
    }

    public void setNat_traversal(boolean nat_traversal) {
        this.nat_traversal = nat_traversal;
    }

    public boolean isOriginal_cdpn_to() {
        return original_cdpn_to;
    }

    public void setOriginal_cdpn_to(boolean original_cdpn_to) {
        this.original_cdpn_to = original_cdpn_to;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isReferred_by_as_cgpn() {
        return referred_by_as_cgpn;
    }

    public void setReferred_by_as_cgpn(boolean referred_by_as_cgpn) {
        this.referred_by_as_cgpn = referred_by_as_cgpn;
    }

    public int getReg_expire_max() {
        return reg_expire_max;
    }

    public void setReg_expire_max(int reg_expire_max) {
        this.reg_expire_max = reg_expire_max;
    }

    public int getReg_expire_min() {
        return reg_expire_min;
    }

    public void setReg_expire_min(int reg_expire_min) {
        this.reg_expire_min = reg_expire_min;
    }

    public boolean isRegistered_domain_to_invite() {
        return registered_domain_to_invite;
    }

    public void setRegistered_domain_to_invite(boolean registered_domain_to_invite) {
        this.registered_domain_to_invite = registered_domain_to_invite;
    }

    public String getRemote_ctr_indication() {
        return remote_ctr_indication;
    }

    public void setRemote_ctr_indication(String remote_ctr_indication) {
        this.remote_ctr_indication = remote_ctr_indication;
    }

    public boolean isReq100rel() {
        return req100rel;
    }

    public void setReq100rel(boolean req100rel) {
        this.req100rel = req100rel;
    }

    public String getRfc4028_control() {
        return rfc4028_control;
    }

    public void setRfc4028_control(String rfc4028_control) {
        this.rfc4028_control = rfc4028_control;
    }

    public String getRouting_context() {
        return routing_context;
    }

    public void setRouting_context(String routing_context) {
        this.routing_context = routing_context;
    }

    public String getSip_domain() {
        return sip_domain;
    }

    public void setSip_domain(String sip_domain) {
        this.sip_domain = sip_domain;
    }

    public String getSip_modifications() {
        return sip_modifications;
    }

    public void setSip_modifications(String sip_modifications) {
        this.sip_modifications = sip_modifications;
    }

    public boolean isSymbol_hash_as_is() {
        return symbol_hash_as_is;
    }

    public void setSymbol_hash_as_is(boolean symbol_hash_as_is) {
        this.symbol_hash_as_is = symbol_hash_as_is;
    }

    public boolean isTel_uri_in_diversion() {
        return tel_uri_in_diversion;
    }

    public void setTel_uri_in_diversion(boolean tel_uri_in_diversion) {
        this.tel_uri_in_diversion = tel_uri_in_diversion;
    }

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public boolean isTrusted_ip() {
        return trusted_ip;
    }

    public void setTrusted_ip(boolean trusted_ip) {
        this.trusted_ip = trusted_ip;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }
}}