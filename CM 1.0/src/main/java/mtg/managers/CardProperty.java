/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.managers;

import io.magicthegathering.javasdk.resource.Card;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.text.Text;
import mtg.enums.E_PropertyName;

/**
 *
 * @author tates
 */
public class CardProperty
{

    private E_PropertyName propName;
    private final SimpleStringProperty propValue;

    public CardProperty(E_PropertyName propName, String propVal)
    {
        this.propName = propName;
        this.propValue = new SimpleStringProperty(propVal);
    }

    public String getPropName()
    {
        return propName.getName();
    }

    public Text getPropValue()
    {
        Text t = new Text(propValue.get());

        t.setWrappingWidth(496);
        return t;
    }

    public void setPropName(E_PropertyName propName)
    {
        this.propName = propName;
    }

    public void setPropValue(String propVal)
    {
        this.propValue.set(propVal);
    }

    public static CardProperty getCardProperty(Card card, E_PropertyName p)
    {
        String propVal = "No data";
        StringBuilder sb;
        try
        {
            switch (p)
            {
                case ID:
                    return new CardProperty(p, card.getId());
                case LAYOUT:
                    return new CardProperty(p, card.getLayout());
                case NAME:
                    return new CardProperty(p, card.getName());
                case NAMES:
                    sb = new StringBuilder();
                    for (String s : card.getNames())
                    {
                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case MANA_COST:
                    return new CardProperty(p, card.getManaCost());
                case CMC:
                    return new CardProperty(p, String.valueOf(card.getCmc()));
                case COLOURS:
                    sb = new StringBuilder();
                    for (String s : card.getColors())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case COLOUR_IDENTITY:
                    sb = new StringBuilder();
                    for (String s : card.getColorIdentity())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case TYPE:
                    return new CardProperty(p, card.getType());
                case SUPERTYPES:
                    sb = new StringBuilder();
                    for (String s : card.getSupertypes())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case TYPES:
                    sb = new StringBuilder();
                    for (String s : card.getTypes())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case SUBTYPES:
                    sb = new StringBuilder();
                    for (String s : card.getSubtypes())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case RARITY:
                    return new CardProperty(p, card.getRarity());
                case TEXT:
                    return new CardProperty(p, card.getRarity());
                case ORIGINAL_TEXT:
                    return new CardProperty(p, card.getOriginalText());
                case FLAVOUR:
                    return new CardProperty(p, card.getFlavor());
                case ARTIST:
                    return new CardProperty(p, card.getArtist());
                case NUMBER:
                    return new CardProperty(p, card.getNumber());
                case POWER:
                    return new CardProperty(p, card.getPower());
                case TOUGHNESS:
                    return new CardProperty(p, card.getToughness());
                case LOYALTY:
                    return new CardProperty(p, String.valueOf(card.getLoyalty()));
                case MULTIVERSE_ID:
                    return new CardProperty(p, String.valueOf(card.getMultiverseid()));
                case VARIOATIONS:
                    sb = new StringBuilder();
                    for (int s : card.getVariations())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case IMAGE_NAME:
                    return new CardProperty(p, card.getImageName());
                case WATERMARK:
                    return new CardProperty(p, card.getWatermark());
                case BORDER:
                    return new CardProperty(p, card.getBorder());
                case TIME_SHIFTED:
                    return new CardProperty(p, String.valueOf(card.isTimeshifted()));
                case HAND:
                    return new CardProperty(p, String.valueOf(card.getHand()));
                case LIFE:
                    return new CardProperty(p, String.valueOf(card.getLife()));
                case RESERVED:
                    return new CardProperty(p, String.valueOf(card.isReserved()));
                case RELEASE_DATE:
                    return new CardProperty(p, card.getReleaseDate());
                case STARTER:
                    return new CardProperty(p, String.valueOf(card.isStarter()));
                case SET:
                    return new CardProperty(p, card.getSet());
                case SET_NAME:
                    return new CardProperty(p, card.getSetName());
                case PRINTINGS:
                    sb = new StringBuilder();
                    for (String s : card.getPrintings())
                    {

                        if (sb.length() > 0)
                        {
                            sb.append("\n");
                        }
                        sb.append(s);
                    }
                    return new CardProperty(p, sb.toString());
                case IMAGE_URL:
                    return new CardProperty(p, card.getImageUrl());
                default:
                    return new CardProperty(p, propVal);
            }
        } catch (Exception e)
        {
            return new CardProperty(p, propVal);
        }
    }
}
