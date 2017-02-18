/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.detail.factory;

/**
 *
 * @author fenriquez
 */
public class DetailFactory {

    public static final int TYPE_DETAIL = 0;
    public static final int TITLE_DETAIL = 1;
    public static final int AUTHORS_DETAIL = 2;
    public static final int STRING_HORIZONTAL_DETAIL = 3;
    public static final int INTEGER_HORIZONTAL_DETAIL = 4;
    public static final int JTA_VERTICAL_DETAIL = 5;
    public static final int JTF_VERTICAL_DETAIL = 6;
    public static final int CATALOG_IDS_DETAIL = 7;
    public static final int ID_DETAIL = 8;

    public Detail getDetail(int type, String key, String interfaceName, long doc_id, Object value, int row) {
        switch (type) {
            case TYPE_DETAIL: {
                return new TypeDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case TITLE_DETAIL: {
                return new TitleDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case AUTHORS_DETAIL: {
                return new AuthorsDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case STRING_HORIZONTAL_DETAIL: {
                return new StringHorizontalDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case INTEGER_HORIZONTAL_DETAIL: {
                return new IntegerHorizontalDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case JTA_VERTICAL_DETAIL: {
                return new JTAVerticalDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case JTF_VERTICAL_DETAIL: {
                return new JTFVerticalDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            case CATALOG_IDS_DETAIL: {
                return new CatalogIDsDetail(key, interfaceName, doc_id, value, row);
            }
            case ID_DETAIL: {
                return new IdentifierDetail(key, interfaceName, doc_id, value.toString(), row);
            }
            default: {
                return new StringHorizontalDetail(key, interfaceName, doc_id, value.toString(), row);
            }
        }
    }
}
