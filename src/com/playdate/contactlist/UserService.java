/*package com.playdate.contactlist;

import java.util.Collections;
import java.util.Vector;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
public class UserService {

    public static Vector<Model_contactlist> getUserList() {
        Vector<Model_contactlist> bookList = new Vector<Model_contactlist>();
        
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(Phone._ID);
            int nameIdx = cursor.getColumnIndex(Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(Phone.NUMBER);
            int photoIdIdx = cursor.getColumnIndex(Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                //...
            } while (cursor.moveToNext());  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        
        

        Model_contactlist book = new Model_contactlist("Things Fall Apart ", "Chinua Achebe ", "1958", " Nigeria ", "English");
        bookList.add(book);
        book = new Model_contactlist("Fairy tales ", "Hans Christian Andersen ", "1835�37 ", "Denmark ", "Danish");
        bookList.add(book);
        
        book = new Model_contactlist("511352312 Fall Apart ", "Chinua Achebe ", "1958", " Nigeria ", "English");
        bookList.add(book);
      
        book = new Model_contactlist("The Divine Comedy ", "Dante Alighieri ", "1265�1321 ", "Florence ", "Italian");
        bookList.add(book);
        book = new Model_contactlist("Epic of Gilgamesh ", "Anonymous ", "18th � 17th century BC ", "Sumer and Akkadian Empire ", "Akkadian");
        bookList.add(book);
        book = new Model_contactlist("Book of Job ", "Anonymous ", "6th � 4th century BC ", "Achaemenid Empire ", "Hebrew");
        bookList.add(book);
        book = new Model_contactlist("One Thousand and One Nights ", "Anonymous ", "700�1500 ", "India/Iran/Iraq/Egypt ", "Arabic");
        bookList.add(book);
        book = new Model_contactlist("Nj�l's Saga ", "Anonymous ", "13th century ", "Iceland ", "Old Norse");
        bookList.add(book);
        book = new Model_contactlist("Pride and Prejudice ", "Jane Austen ", "1813", " United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Le P�re Goriot ", "Honor� de Balzac ", "1835", " France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Molloy, Malone Dies, The Unnamable, a trilogy ", "Samuel Beckett ", "1951�53 ", "Republic of Ireland ", "French, English");
        bookList.add(book);
        book = new Model_contactlist("The Decameron ", "Giovanni Boccaccio ", "1349�53 ", "Ravenna ", "Italian");
        bookList.add(book);
        book = new Model_contactlist("Ficciones ", "Jorge Luis Borges ", "1944�86 ", "Argentina ", "Spanish");
        bookList.add(book);
        book = new Model_contactlist("Wuthering Heights ", "Emily Bront� ", "1847", "  United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("The Stranger ", "Albert Camus ", "1942", "   Algeria, French Empire ", "French");
        bookList.add(book);
        book = new Model_contactlist("Poems ", "Paul Celan ", "1952", "    Romania, France ", "German");
        bookList.add(book);
        book = new Model_contactlist("Journey to the End of the Night ", "Louis-Ferdinand C�line ", "1932", "  France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Don Quixote ", "Miguel de Cervantes ", "1605 (part 1), 1615 (part 2) ", "Spain ", "Spanish");
        bookList.add(book);
        book = new Model_contactlist("The Canterbury Tales ", "Geoffrey Chaucer ", "14th century ", "England ", "English");
        bookList.add(book);
        book = new Model_contactlist("Stories ", "Anton Chekhov ", "1886", "   Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("Nostromo ", "Joseph Conrad ", "1904", "  United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Great Expectations ", "Charles Dickens ", "1861", "  United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Jacques the Fatalist ", "Denis Diderot ", "1796", "  France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Berlin Alexanderplatz ", "Alfred D�blin ", "1929", " Germany ", "German");
        bookList.add(book);
        book = new Model_contactlist("Crime and Punishment ", "Fyodor Dostoyevsky ", "1866", " Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("The Idiot ", "Fyodor Dostoyevsky ", "1869", "    Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("The Possessed ", "Fyodor Dostoyevsky ", "1872", "    Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("The Brothers Karamazov ", "Fyodor Dostoyevsky ", "1880", "   Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("Middlemarch ", "George Eliot ", "1871", "    United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Invisible Man ", "Ralph Ellison ", "1952", " United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Medea ", "Euripides ", "431 BC ", "Athens ", "Classical Greek");
        bookList.add(book);
        book = new Model_contactlist("Absalom, Absalom! ", "William Faulkner ", "1936", "  United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("The Sound and the Fury ", "William Faulkner ", "1929", " United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Madame Bovary ", "Gustave Flaubert ", "1857", "  France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Sentimental Education ", "Gustave Flaubert ", "1869", "  France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Gypsy Ballads ", "Federico Garc�a Lorca ", "1928", " Spain ", "Spanish");
        bookList.add(book);
        book = new Model_contactlist("One Hundred Years of Solitude ", "Gabriel Garc�a M�rquez ", "1967", "    Colombia ", "Spanish");
        bookList.add(book);
        book = new Model_contactlist("Love in the Time of Cholera ", "Gabriel Garc�a M�rquez ", "1985", "  Colombia ", "Spanish");
        bookList.add(book);
        book = new Model_contactlist("Faust ", "Johann Wolfgang von Goethe ", "1832", "    Saxe-Weimar, Germany ", "German");
        bookList.add(book);
        book = new Model_contactlist("Dead Souls ", "Nikolai Gogol ", "1842", "    Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("The Tin Drum ", "G�nter Grass ", "1959", "   West Germany ", "German");
        bookList.add(book);
        book = new Model_contactlist("The Devil to Pay in the Backlands ", "Jo�o Guimar�es Rosa ", "1956", "   Brazil ", "Portuguese");
        bookList.add(book);
        book = new Model_contactlist("Hunger ", "Knut Hamsun ", "1890", "  Norway ", "Norwegian");
        bookList.add(book);
        book = new Model_contactlist("The Old Man and the Sea ", "Ernest Hemingway ", "1952", "    United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Iliad ", "Homer ", "850�750 BC ", "Possibly Smyrna ", "Classical Greek");
        bookList.add(book);
        book = new Model_contactlist("Odyssey ", "Homer ", "8th century BC ", "Possibly Smyrna ", "Classical Greek");
        bookList.add(book);
        book = new Model_contactlist("A Doll's House ", "Henrik Ibsen ", "1879", " Norway ", "Norwegian");
        bookList.add(book);
        book = new Model_contactlist("Ulysses ", "James Joyce ", "1922", " Irish Free State ", "English");
        bookList.add(book);
        book = new Model_contactlist("Stories ", "Franz Kafka ", "1924", " Austria ", "German");
        bookList.add(book);
        book = new Model_contactlist("The Trial ", "Franz Kafka ", "1925", "   Austria ", "German");
        bookList.add(book);
        book = new Model_contactlist("The Castle ", "Franz Kafka ", "1926", "  Austria ", "German");
        bookList.add(book);
        book = new Model_contactlist("Shakuntala ", "Kalidasa ", "1st century BC � 4th century AD ", "India ", "Sanskrit");
        bookList.add(book);
        book = new Model_contactlist("The Sound of the Mountain ", "Yasunari Kawabata ", "1954", " Japan ", "Japanese");
        bookList.add(book);
        book = new Model_contactlist("Zorba the Greek ", "Nikos Kazantzakis ", "1946", "   Greece ", "Greek");
        bookList.add(book);
        book = new Model_contactlist("Sons and Lovers ", "D. H. Lawrence ", "1913", "  United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Independent People ", "Halld�r Laxness ", "1934�35 ", "Iceland ", "Icelandic");
        bookList.add(book);
        book = new Model_contactlist("Poems ", "Giacomo Leopardi ", "1818", "  Italy ", "Italian");
        bookList.add(book);
        book = new Model_contactlist("The Golden Notebook ", "Doris Lessing ", "1962", "   United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Pippi Longstocking ", "Astrid Lindgren ", "1945", "  Sweden ", "Swedish");
        bookList.add(book);
        book = new Model_contactlist("A Madman's Diary ", "Lu Xun ", "1918", " China ", "Chinese");
        bookList.add(book);
        book = new Model_contactlist("Children of Gebelawi ", "Naguib Mahfouz ", "1959", " Egypt ", "Arabic");
        bookList.add(book);
        book = new Model_contactlist("Buddenbrooks ", "Thomas Mann ", "1901", "    Germany ", "German");
        bookList.add(book);
        book = new Model_contactlist("The Magic Mountain ", "Thomas Mann ", "1924", "  Germany ", "German");
        bookList.add(book);
        book = new Model_contactlist("Moby-Dick ", "Herman Melville ", "1851", "   United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Essays ", "Michel de Montaigne ", "1595", "  France ", "French");
        bookList.add(book);
        book = new Model_contactlist("History ", "Elsa Morante ", "1974", "    Italy ", "Italian");
        bookList.add(book);
        book = new Model_contactlist("Beloved ", "Toni Morrison ", "1987", "   United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("The Tale of Genji ", "Murasaki Shikibu ", "11th century ", "Japan ", "Japanese");
        bookList.add(book);
        book = new Model_contactlist("The Man Without Qualities ", "Robert Musil ", "1930�32 ", "Austria ", "German");
        bookList.add(book);
        book = new Model_contactlist("Lolita ", "Vladimir Nabokov ", "1955", " Russia/United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Nineteen Eighty-Four ", "George Orwell ", "1949", "  United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Metamorphoses ", "Ovid ", "1st century AD ", "Roman Empire ", "Classical Latin");
        bookList.add(book);
        book = new Model_contactlist("The Book of Disquiet ", "Fernando Pessoa ", "1928", "    Portugal ", "Portuguese");
        bookList.add(book);
        book = new Model_contactlist("Tales ", "Edgar Allan Poe ", "19th century ", "United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("In Search of Lost Time ", "Marcel Proust ", "1913�27 ", "France ", "French");
        bookList.add(book);
        book = new Model_contactlist("The Life of Gargantua and of Pantagruel ", "Fran�ois Rabelais ", "1532�34 ", "France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Pedro P�ramo ", "Juan Rulfo ", "1955", " Mexico ", "Spanish");
        bookList.add(book);
        book = new Model_contactlist("Masnavi ", "Rumi ", "1258�73 ", "Persia, Mongol Empire ", "Persian");
        bookList.add(book);
        book = new Model_contactlist("Midnight's Children ", "Salman Rushdie ", "1981", "  United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Bostan ", "Saadi ", "1257", "    Persia, Mongol Empire ", "Persian");
        bookList.add(book);
        book = new Model_contactlist("Season of Migration to the North ", "Tayeb Salih ", "1971", "    Sudan ", "Arabic");
        bookList.add(book);
        book = new Model_contactlist("Blindness ", "Jos� Saramago ", "1995", " Portugal ", "Portuguese");
        bookList.add(book);
        book = new Model_contactlist("Hamlet ", "William Shakespeare ", "1603", "  England ", "English");
        bookList.add(book);
        book = new Model_contactlist("King Lear ", "William Shakespeare ", "1608", "   England ", "English");
        bookList.add(book);
        book = new Model_contactlist("Othello ", "William Shakespeare ", "1609", " England ", "English");
        bookList.add(book);
        book = new Model_contactlist("Oedipus the King ", "Sophocles ", "430 BC ", "Athens ", "Classical Greek");
        bookList.add(book);
        book = new Model_contactlist("The Red and the Black ", "Stendhal ", "1830", "  France ", "French");
        bookList.add(book);
        book = new Model_contactlist("Tristram Shandy ", "Laurence Sterne ", "1760", " England ", "English");
        bookList.add(book);
        book = new Model_contactlist("Confessions of Zeno ", "Italo Svevo ", "1923", " Italy ", "Italian");
        bookList.add(book);
        book = new Model_contactlist("Gulliver's Travels ", "Jonathan Swift ", "1726", "   Ireland ", "English");
        bookList.add(book);
        book = new Model_contactlist("War and Peace ", "Leo Tolstoy ", "1865�1869 ", "Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("Anna Karenina ", "Leo Tolstoy ", "1877", "   Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("The Death of Ivan Ilyich ", "Leo Tolstoy ", "1886", "    Russia ", "Russian");
        bookList.add(book);
        book = new Model_contactlist("Adventures of Huckleberry Finn ", "Mark Twain ", "1884", "   United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Ramayana ", "Valmiki ", "3rd century BC � 3rd century AD ", "India ", "Sanskrit");
        bookList.add(book);
        book = new Model_contactlist("Aeneid ", "Virgil ", "29�19 BC ", "Roman Empire ", "Classical Latin");
        bookList.add(book);
        book = new Model_contactlist("Mahabharata ", "Vyasa ", "4th century BC � 4th century AD ", "India ", "Sanskrit");
        bookList.add(book);
        book = new Model_contactlist("Leaves of Grass ", "Walt Whitman ", "1855", "    United States ", "English");
        bookList.add(book);
        book = new Model_contactlist("Mrs Dalloway ", "Virginia Woolf ", "1925", " United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("To the Lighthouse ", "Virginia Woolf ", "1927", "    United Kingdom ", "English");
        bookList.add(book);
        book = new Model_contactlist("Memoirs of Hadrian ", "Marguerite Yourcenar ", "1951", " France ", "French");
        bookList.add(book);
        book = new Model_contactlist("9228241614 Fall Apart ", "Chinua Achebe ", "1958", " Nigeria ", "English");
        bookList.add(book);
        Collections.sort(bookList);
        return bookList;
    }

	

}
*/