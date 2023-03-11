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

    @XmlAttribute
    private boolean active;
    @XmlAttribute
    private boolean alarm_enable;
    @XmlAttribute(name = "aliases_list")
    private String aliases_list;
    @XmlAttribute
    private String auth;
    @XmlAttribute
    private boolean auth_qop;
    @XmlAttribute
    private String auto_answer_version;
    @XmlAttribute
    private String category_to_sip;
    @XmlAttribute
    private String contact;
    @XmlAttribute
    private boolean digest;
    @XmlAttribute
    private String display_encoding;
    @XmlAttribute
    private String domain;
    @XmlAttribute
    private boolean dtmf_relay;
    @XmlAttribute
    private String fork_mode;
    @XmlAttribute
    private String group;
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String iface;
    @XmlAttribute
    private String last_via;
    @XmlAttribute
    private String login;
    @XmlAttribute
    private String my_from;
    @XmlAttribute
    private boolean nat_traversal;
    @XmlAttribute
    private boolean original_cdpn_to;
    @XmlAttribute
    private String owner;
    @XmlAttribute
    private String password;
    @XmlAttribute
    private boolean referred_by_as_cgpn;
    @XmlAttribute
    private int reg_expire_max;
    @XmlAttribute
    private int reg_expire_min;
    @XmlAttribute
    private boolean registered_domain_to_invite;
    @XmlAttribute
    private String remote_ctr_indication;
    @XmlAttribute
    private boolean req100rel;
    @XmlAttribute
    private String rfc4028_control;
    @XmlAttribute
    private String routing_context;
    @XmlAttribute
    private String sip_domain;
    @XmlAttribute
    private String sip_modifications;
    @XmlAttribute
    private boolean symbol_hash_as_is;
    @XmlAttribute
    private boolean tel_uri_in_diversion;
    @XmlAttribute
    private String transit;
    @XmlAttribute
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