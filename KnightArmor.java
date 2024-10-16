
/*
 * Номер залікової книжки: 19
 * C2 (остача від ділення на 2): 1
 * C3 (остача від ділення на 3): 1
 * Інтерфейс: Set
 * Внутрішня структура колекції: Однозв’язний список
 * Завдання: Визначити ієрархію амуніції лицаря. Екіпірувати лицаря. Порахувати
 * вартість амуніції. Провести сортування амуніції за вагою. Знайти елементи
 * амуніції, що відповідає заданому діапазону цін.
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

// Абстрактний клас для амуніції
abstract class Equipment {
    private String name;
    private double weight; // вага в кілограмах
    private double price; // ціна в одиницях валюти

    public Equipment(String name, double weight, double price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s: вага = %.2f кг, цiна = %.2f", name, weight, price);
    }
}

// Клас для броні
class Armor extends Equipment {
    public Armor(String name, double weight, double price) {
        super(name, weight, price);
    }
}

// Клас для шолома
class Helmet extends Equipment {
    public Helmet(String name, double weight, double price) {
        super(name, weight, price);
    }
}

// Клас для щита
class Shield extends Equipment {
    public Shield(String name, double weight, double price) {
        super(name, weight, price);
    }
}

// Клас TypedCollection, що реалізує інтерфейс Set та використовує однозв'язний
// список
class TypedCollection<T> implements Set<T> {
    private Node<T> head;
    private int size;

    // Внутрішній клас для вузла однозв'язного списку
    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }

    // Порожній конструктор
    public TypedCollection() {
        head = null;
        size = 0;
    }

    // Конструктор з одним об'єктом
    public TypedCollection(T element) {
        this();
        add(element);
    }

    // Конструктор зі стандартною колекцією
    public TypedCollection(Collection<? extends T> collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean add(T element) {
        if (contains(element)) {
            return false;
        }
        Node<T> newNode = new Node<>(element);
        newNode.next = head;
        head = newNode;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> current = head;
        Node<T> prev = null;

        while (current != null) {
            if (current.data.equals(o)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    public <E> E[] toArray(E[] array) {
        if (array.length < size) {
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = (E) current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<T> current = head;
        Node<T> prev = null;

        while (current != null) {
            if (!c.contains(current.data)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                modified = true;
            } else {
                prev = current;
            }
            current = current.next;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }
}

// Клас для управління амуніцією лицаря
class KnightEquipmentManager {
    private TypedCollection<Equipment> equipmentCollection;

    public KnightEquipmentManager() {
        this.equipmentCollection = new TypedCollection<>();
    }

    public KnightEquipmentManager(Equipment equipment) {
        this.equipmentCollection = new TypedCollection<>(equipment);
    }

    public KnightEquipmentManager(Collection<Equipment> collection) {
        this.equipmentCollection = new TypedCollection<>(collection);
    }

    public void addEquipment(Equipment equipment) {
        equipmentCollection.add(equipment);
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (Equipment equipment : equipmentCollection) {
            total += equipment.getPrice();
        }
        return total;
    }

    public void sortByWeight() {
        Equipment[] array = equipmentCollection.toArray(new Equipment[0]);
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j].getWeight() > array[j + 1].getWeight()) {
                    Equipment temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        equipmentCollection.clear();
        for (Equipment e : array) {
            equipmentCollection.add(e);
        }
    }

    // Пошук амуніції за діапазоном цін
    public void findEquipmentByPriceRange(double minPrice, double maxPrice) {
        for (Equipment equipment : equipmentCollection) {
            if (equipment.getPrice() >= minPrice && equipment.getPrice() <= maxPrice) {
                System.out.println(equipment);
            }
        }
    }

    public void printEquipment() {
        for (Equipment equipment : equipmentCollection) {
            System.out.println(equipment);
        }
    }
}

// Основний клас для запуску програми
public class KnightArmor {
    public static void main(String[] args) {
        KnightEquipmentManager manager = new KnightEquipmentManager();

        // Додаємо амуніцію
        manager.addEquipment(new Armor("Кольчужна броня", 12.5, 150));
        manager.addEquipment(new Helmet("Шолом лицаря", 3.0, 75));
        manager.addEquipment(new Shield("Дерев'яний щит", 5.0, 30));
        manager.addEquipment(new Armor("Бойовий панцир", 18.0, 300));

        // Виведення амуніції
        System.out.println("Екiпiрування лицаря:");
        manager.printEquipment();

        // Розрахунок загальної вартості
        System.out.printf("Загальна вартiсть амунiцiї: %.2f\n", manager.calculateTotalPrice());

        // Сортування амуніції за вагою
        System.out.println("\nАмунiцiя пiсля сортування за вагою:");
        manager.sortByWeight();
        manager.printEquipment();

        // Пошук амуніції за діапазоном цін
        System.out.println("\nАмунiцiя в дiапазонi цін 50 - 200:");
        manager.findEquipmentByPriceRange(50, 200);
    }
}
