/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Konstantinos Meliras, Konstantinos Kontovas, Stamatis Asterios
 * 
 * 
 */

//Class
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
    @Column(name = "DRAWID")        //Number of draw
    private Integer drawid;
    @Column(name = "DRAWIDTIME")    //Date of draw
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date drawidtime;
    @Column(name = "FIRSTNUMBER")   //First number of draw
    private Integer firstnumber;
    @Column(name = "SECONDNUMBER")  //Second number of draw
    private Integer secondnumber;
    @Column(name = "THIRDNUMBER")   //Third number of draw
    private Integer thirdnumber;
    @Column(name = "FOURTHNUMBER")  //Fourth number of draw
    private Integer fourthnumber;
    @Column(name = "FIFTHNUMBER")   //Fifth number of draw
    private Integer fifthnumber;
    @Column(name = "JOKER")         //Joker number of draw
    private Integer joker;
    @OneToMany(mappedBy = "drawid", cascade = CascadeType.ALL)
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
        this.drawidtime = convertTime(drawidtime); //Call method to convert Date 
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

    //Method to get the total distributed money
    public double getdistributedMoney() {
        double totalDistributed = 0;
        Collection<Prizecategory> prizeCat = getPrizecategoryCollection();
        for (Prizecategory pc : prizeCat) {
            if ((pc.getIdcategory() == 1) || (pc.getIdcategory() == 2)) { //For the first two categories we also add the jackpot
                totalDistributed = totalDistributed + pc.getDistributed() + pc.getJackpot();
            } else {
                totalDistributed += pc.getDistributed();
            }
        }
        return  totalDistributed;
    }
    
    //Method to check if we had jackpot in draw, we check only the first 2 Prize categories.
    public int getjackpots() {
        for (Prizecategory pc : prizecategoryCollection) {  
            if ((pc.getIdcategory() == 1) && (pc.getWinners() == 0)) {
                return 1;
            }
        }
        return 0;
    }
    //Method to convert Date from Unix format to human date
    public Date convertTime(long drawTimeUnix) {
        Date date = new Date(drawTimeUnix);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return date;
    }

}
