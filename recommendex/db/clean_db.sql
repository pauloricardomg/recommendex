DELETE FROM Transaction_Item;

DELETE FROM Transaction;

DELETE FROM Item;

DELETE FROM User WHERE admin=FALSE;
