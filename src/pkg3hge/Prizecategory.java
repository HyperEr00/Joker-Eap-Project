/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hyperer
 */
@Entity
@Table(name = "PRIZECATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prizecategory.findAll", query = "SELECT p FROM Prizecategory p")
    , @NamedQuery(name = "Prizecategory.findById", query = "SELECT p FROM Prizecategory p WHERE p.id = :id")
    , @NamedQuery(name = "Prizecategory.findByIdcategory", query = "SELECT p FROM Prizecategory p WHERE p.idcategory = :idcategory")
    , @NamedQuery(name = "Prizecategory.findByDistributed", query = "SELECT p FROM Prizecategory p WHERE p.distributed = :distributed")
    , @NamedQuery(name = "Prizecategory.findByDivident", query = "SELECT p FROM Prizecategory p WHERE p.divident = :divident")
    , @NamedQuery(name = "Prizecategory.findByJackpot", query = "SELECT p FROM Prizecategory p WHERE p.jackpot = :jackpot")
    , @NamedQuery(name = "Prizecategory.findByWinners", query = "SELECT p FROM Prizecategory p WHERE p.winners = :winners")})
public class Prizecategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "IDCATEGORY")
    private Integer idcategory;
    @Column(name = "DISTRIBUTED")
    private Double distributed;
    @Column(name = "DIVIDENT")
    private Double divident;
    @Column(name = "JACKPOT")
    private Double jackpot;
    @Column(name = "WINNERS")
    private Integer winners;
    @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID")
    @ManyToOne
    private Draw drawid;

    public Prizecategory() {
    }

    public Prizecategory(Long id) {
        this.id = id;
    }

    public Prizecategory(Integer idcategory, Double distributed, Double divident, Double jackpot, Integer winners, Draw drawid) {
        this.idcategory = idcategory;
        this.distributed = distributed;
        this.divident = divident;
        this.jackpot = jackpot;
        this.winners = winners;
        this.drawid = drawid;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(Integer idcategory) {
        this.idcategory = idcategory;
    }

    public Double getDistributed() {
        return distributed;
    }

    public void setDistributed(Double distributed) {
        this.distributed = distributed;
    }

    public Double getDivident() {
        return divident;
    }

    public void setDivident(Double divident) {
        this.divident = divident;
    }

    public Double getJackpot() {
        return jackpot;
    }

    public void setJackpot(Double jackpot) {
        this.jackpot = jackpot;
    }

    public Integer getWinners() {
        return winners;
    }

    public void setWinners(Integer winners) {
        this.winners = winners;
    }

    public Draw getDrawid() {
        return drawid;
    }

    public void setDrawid(Draw drawid) {
        this.drawid = drawid;
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
        if (!(object instanceof Prizecategory)) {
            return false;
        }
        Prizecategory other = (Prizecategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pkg3hge.Prizecategory[ id=" + id + " ]";
    }
    
}
