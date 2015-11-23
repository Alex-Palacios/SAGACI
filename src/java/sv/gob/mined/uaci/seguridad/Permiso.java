/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.uaci.seguridad;

import java.io.Serializable;

/**
 *
 * @author ALEX
 */
public class Permiso implements Serializable {
    private boolean insert;
    private boolean update;
    private boolean delete;

    public Permiso() {
        this.insert = false;
        this.update = false;
        this.delete = false;
    }

    
    public Permiso(boolean insert, boolean update, boolean delete) {
        this.insert = insert;
        this.update = update;
        this.delete = delete;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    
    
}
