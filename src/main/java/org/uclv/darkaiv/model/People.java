/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author fenriquez
 */
@DbName("repository")
@Cached
@Table("people")
public class People extends Model {
}
