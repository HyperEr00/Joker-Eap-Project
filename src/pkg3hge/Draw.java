/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Hyperer
 */
@Entity
@Table(name = "DRAW")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Draw.findAll", query = "SELECT d FROM Draw d")
    , @NamedQuery(name = "Draw.findById", query = "SELECT d FROM Draw d WHERE d.id = :id")
    , @NamedQuery(name = "Draw.findByDrawid", query = "SELECT d FROM Draw d WHERE d.drawid = :drawid")
    , @NamedQuery(name = "Draw.findByDrawIdtime", query = "SELECT d FROM Draw d WHERE d.drawidtime = :drawidtime")        
    , @NamedQuery(name = "Draw.findByFirstnumber", query = "SELECT d FROM Draw d WHERE d.firstnumber = :firstnumber")
    , @NamedQuery(name = "Draw.findBySecondnumber", query = "SELECT d FROM Draw d WHERE d.secondnumber = :secondnumber")
    , @NamedQuery(name = "Draw.findByThirdnumber", query = "SELECT d FROM Draw d WHERE d.thirdnumber = :thirdnumber")
    , @NamedQuery(name = "Draw.findByFourthnumber", query = "SELECT d FROM Draw d WHERE d.fourthnumber = :fourthnumber")
    , @NamedQuery(name = "Draw.findByFifthnumber", query = "SELECT d FROM Draw d WHERE d.fifthnumber = :fifthnumber")
    , @NamedQuery(name = "Draw.findByJoker", query = "SELECT d FROM Draw d WHERE d.joker = :joker")})
public class Draw implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DRAWID")
    private Integer drawid;
    @Column(name = "DRAWIDTIME")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date drawidtime;
    @Column(name = "FIRSTNUMBER")
    private Integer firstnumber;
    @Column(name = "SECONDNUMBER")
    private Integer secondnumber;
    @Column(name = "THIRDNUMBER")
    private Integer thirdnumber;
    @Column(name = "FOURTHNUMBER")
    private Integer fourthnumber;
    @Column(name = "FIFTHNUMBER")
    private Integer fifthnumber;
    @Column(name = "JOKER")
    private Integer joker;
    @OneToMany(mappedBy = "drawid", cascade = CascadeType.PERSIST)
    private Collection<Prizecategory> prizecategoryCollection;

    public Draw() {
    }

    public Draw(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDrawid() {
        return drawid;
    }

    public void setDrawid(Integer drawid) {
        this.drawid = drawid;
    }

    public Date getDrawidtime() {
        return drawidtime;
    }

    public void setDrawidtime(long drawidtime) {
        this.drawidtime = convertTime(drawidtime);
    }

    public Integer getFirstnumber() {
        return firstnumber;
    }

    public void setFirstnumber(Integer firstnumber) {
        this.firstnumber = firstnumber;
    }

    public Integer getSecondnumber() {
        return secondnumber;
    }

    public void setSecondnumber(Integer secondnumber) {
        this.secondnumber = secondnumber;
    }

    public Integer getThirdnumber() {
        return thirdnumber;
    }

    public void setThirdnumber(Integer thirdnumber) {
        this.thirdnumber = thirdnumber;
    }

    public Integer getFourthnumber() {
        return fourthnumber;
    }

    public void setFourthnumber(Integer fourthnumber) {
        this.fourthnumber = fourthnumber;
    }

    public Integer getFifthnumber() {
        return fifthnumber;
    }

    public void setFifthnumber(Integer fifthnumber) {
        this.fifthnumber = fifthnumber;
    }

    public Integer getJoker() {
        return joker;
    }

    public void setJoker(Integer joker) {
        this.joker = joker;
    }

    @XmlTransient
    public Collection<Prizecategory> getPrizecategoryCollection() {
        return prizecategoryCollection;
    }

    public void setPrizecategoryCollection(Collection<Prizecategory> prizecategoryCollection) {
        this.prizecategoryCollection = prizecategoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Draw)) {
            return false;
        }
        Draw other = (Draw) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pkg3hge.Draw[ id=" + id + " ]";
    }
    
    public int distributedMoney() {
        double totalDistributed = 0;
        for (Prizecategory pc : prizecategoryCollection) {
            if ((pc.getId() == 1) || (pc.getId() == 2)) {
                totalDistributed += pc.getDistributed() + pc.getJackpot();
            }
        }
        return (int) totalDistributed;
    }

    public boolean jackpots() {
        for (Prizecategory pc : prizecategoryCollection) {
            if ((pc.getId() == 1) && (pc.getWinners() == 0)) {
                return true;
            }
        }
        return false;
    }

    public Date convertTime(long drawTimeUnix) {
        long unixTime = drawTimeUnix / 1000;
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return date;
    }
    
}
