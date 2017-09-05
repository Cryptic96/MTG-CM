/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.enums;

/**
 *
 * @author tates
 */
public enum E_PropertyName
{
    ID("ID"),
    LAYOUT("Layout"),
    NAME("Name"),
    NAMES("Names"),
    MANA_COST("Mana cost"),
    CMC("CMC"),
    COLOURS("Colours"),
    COLOUR_IDENTITY("Colour Identity"),
    TYPE("Type"),
    SUPERTYPES("Supertypes"),
    TYPES("Types"),
    SUBTYPES("SubTypes"),
    RARITY("Rarity"),
    TEXT("Text"),
    ORIGINAL_TEXT("Original Text"),
    FLAVOUR("Flavour"),
    ARTIST("Artist"),
    NUMBER("Number"),
    POWER("Power"),
    TOUGHNESS("Toughness"),
    LOYALTY("Loyalty"),
    MULTIVERSE_ID("Multiverse ID"),
    VARIOATIONS("Variations"),
    IMAGE_NAME("Image Name"),
    WATERMARK("Watermark"),
    BORDER("Border"),
    TIME_SHIFTED("Time Shifted"),
    HAND("Hand"),
    LIFE("Life"),
    RESERVED("Reserved"),
    RELEASE_DATE("Release Date"),
    STARTER("Starter"),
    SET("Set"),
    SET_NAME("Setname"),
    PRINTINGS("Printings"),
    IMAGE_URL("Image URL");

    private final String name;

    E_PropertyName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public E_PropertyName getPropertyNameEnum(String name)
    {
        for (E_PropertyName p : E_PropertyName.values())
        {
            if (p.getName().equals(name))
            {
                return p;
            }
        }
        return null;
    }
}
