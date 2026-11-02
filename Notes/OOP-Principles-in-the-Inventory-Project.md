# OOP Principles in the Inventory & Stock Management Project

How the four OOP principles show up in our SDA semester project (Java Swing front end, SQL back end). Class names and line numbers refer to the project source.

## 1. ENCAPSULATION

**Definition**: Bundling of data (attributes) and methods that operate on the data within a single unit (class), while restricting direct access to some of the object's components.

### Implementation

**Model Classes with Private Fields**:

```java
// Product.java (Lines 3-62)
public class Product {
    private int productId;           // Encapsulated field
    private String productName;      // Encapsulated field
    private int categoryId;          // Encapsulated field
    private double price;            // Encapsulated field
    private int stockQuantity;       // Encapsulated field

    // Public getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }

    // Public setters
    public void setProductId(int productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
}
```

**All Model Classes Following This Pattern**:
- User.java - Encapsulates user credentials and role
- Supplier.java - Encapsulates supplier information
- StockInflow.java - Encapsulates inflow transaction data
- StockOutflow.java - Encapsulates outflow transaction data
- ReorderRequest.java - Encapsulates reorder request details
- LowStockItem.java - Encapsulates low stock alert data

**Database Handler Encapsulation**:

```java
// DBHandler.java (Lines 7-42)
public class DBHandler {
    // Encapsulated credentials
    private static final String URL = "jdbc:mysql://localhost:3306/...";
    private static final String USER = "root";
    private static final String PASS = "root123";

    // Encapsulated instance
    private static DBHandler instance;

    // Private constructor prevents external instantiation
    private DBHandler() {}

    // Controlled access through public method
    public static synchronized DBHandler getInstance() {
        if (instance == null) {
            instance = new DBHandler();
        }
        return instance;
    }
}
```

### Benefits
- **Data Integrity**: Fields cannot be modified arbitrarily
- **Controlled Access**: Validation can be added in setters
- **Information Hiding**: Internal implementation details are hidden
- **Maintainability**: Changes to internal structure don't affect external code

---

## 2. ABSTRACTION

**Definition**: Hiding complex implementation details and showing only the essential features of an object through interfaces and abstract classes.

### Implementation

**Service Layer Interfaces**:

**1. LoginService Interface**
```java
// LoginService.java (Lines 3-11)
public interface LoginService {
    String validateCredentials(String username, String password);
    boolean isUserActive(String username);
}
```

Abstracts authentication logic - clients don't need to know about SQL queries or password validation

**2. ProductService Interface**
```java
// ProductService.java (Lines 6-48)
public interface ProductService {
    // Category operations (abstracted)
    List<String> loadAllCategoryNames();
    Map<String, Integer> loadCategoryNameIdMap();
    boolean addCategory(String categoryName);
    boolean deleteCategory(int categoryId);

    // Product CRUD operations (abstracted)
    boolean saveProduct(String name, double price, int stock, int categoryId);
    boolean deleteProduct(int productId);
    boolean updateProduct(int productId, String newName, int newCategoryId, double newPrice);

    // Search operations (abstracted)
    List<Map<String, Object>> searchProducts(String keyword);
    List<String> getSearchSuggestions(String prefix);
}
```

Abstracts complex product management - UI doesn't know about database operations

**3. StockService Interface**
```java
// StockService.java (Lines 64-112)
public interface StockService {
    // Data loading (abstracted)
    List<String> loadProducts();
    List<Integer> loadProductIds();
    List<String> loadSuppliers();
    List<Integer> loadSupplierIds();

    // Stock operations (abstracted)
    boolean recordStockInflow(int productId, int quantityReceived,
                              int supplierId, String date);
    boolean recordStockOutflow(int productId, int quantity, String reason);

    // Low stock monitoring (abstracted)
    List<LowStockItem> loadLowStockItems();

    // Reorder management (abstracted)
    boolean createReorderRequest(int productId, int supplierId, int requestedQty);

    // Observer pattern (abstracted)
    void addStockObserver(Runnable observer);
    void notifyStockObservers();
}
```

Abstracts stock management complexity - observers don't know how stock is stored

**4. UserService Interface**
```java
// UserService.java (Lines 6-25)
public interface UserService {
    List<Map<String, Object>> loadUsers();
    boolean addUser(String username, String password);
    boolean deactivateUser(int userId);
    boolean deleteUser(int userId);
    boolean activateUser(int userId);
    boolean isUsernameTaken(String username);
}
```

**5. UI Observer Interface**
```java
// ProductUpdateListener.java (Lines 3-5)
public interface ProductUpdateListener {
    void onProductUpdated();  // Abstract notification mechanism
}
```

### Benefits
- **Reduced Complexity**: Complex SQL and business logic hidden from UI
- **Flexibility**: Can swap implementations without changing client code
- **Testing**: Easy to create mock implementations for testing
- **Parallel Development**: Teams can work on interfaces before implementation

---

## 3. INHERITANCE

**Definition**: Mechanism where a new class derives properties and behaviors from an existing class, establishing an "is-a" relationship.

### Implementation

**Swing Framework Inheritance**:

```java
// MainFrame.java (Line 6)
public class MainFrame extends JFrame {
    // Inherits all JFrame functionality:
    // - setTitle(), setSize(), setDefaultCloseOperation()
    // - add(), pack(), setVisible()
    // - Window management, event handling
}

// DashboardPanel.java (Line 11)
public class DashboardPanel extends JPanel {
    // Inherits all JPanel functionality:
    // - setLayout(), add(), setBackground()
    // - Component management, painting
}
```

**All UI Classes Using Inheritance**:

| Class | Extends | Inherited Capabilities |
|-------|---------|----------------------|
| MainFrame | JFrame | Window management, title bar, close button |
| DashboardPanel | JPanel | Container functionality, layout management |
| LoginPanel | JPanel | Component container, event handling |
| AddProductPanel | JPanel | UI component management |
| CategoryPanel | JPanel | Layout and component support |
| DeleteProductPanel | JPanel | Panel rendering and events |
| HomePanel | JPanel | Container capabilities |
| LowStockPanel | JPanel | Component management |
| ProductManagementPanel | JPanel | Layout support |
| ProductSearchPanel | JPanel | UI container functionality |
| ReportsPanel | JPanel | Component rendering |
| StockInflowPanel | JPanel | Event and layout management |
| StockManagementPanel | JPanel | Panel capabilities |
| StockOutflowPanel | JPanel | Container functionality |
| SupplierPanel | JPanel | UI component support |
| UpdateProductPanel | JPanel | Layout and rendering |
| UserManagementPanel | JPanel | Panel management |

### Benefits
- **Code Reuse**: Don't need to reimplement basic window/panel functionality
- **Polymorphism**: Can treat all panels as JPanel objects
- **Consistency**: All UI components follow same patterns
- **Framework Integration**: Seamless integration with Swing event system

---

## 4. POLYMORPHISM

**Definition**: Ability of objects of different classes to respond to the same method call in different ways, enabling a single interface to represent different underlying forms.

### Implementation

**Interface-Based Polymorphism (Runtime)**:

```java
// Reference type = Interface, Object type = Implementation
LoginService loginService = new LoginServiceImpl();
ProductService productService = new ProductServiceImpl();
StockService stockService = new StockServiceImpl();
UserService userService = new UserServiceImpl();
SupplierService supplierService = new SupplierServiceImpl();
ReportsService reportsService = new ReportsServiceImpl();
```

**Example: ProductServiceImpl**
```java
// ProductServiceImpl.java (Line 7)
public class ProductServiceImpl implements ProductService {

    @Override  // Method overriding
    public List<String> loadAllCategoryNames() {
        // Specific implementation for loading categories
        List<String> categories = new ArrayList<>();
        String sql = "SELECT category_name FROM categories ORDER BY category_id ASC";
        // ... database logic
        return categories;
    }

    @Override  // Method overriding
    public boolean saveProduct(String name, double price, int stock, int categoryId) {
        // Specific implementation for saving products
        int newId = findAvailableProductId();
        String sql = "INSERT INTO products ...";
        // ... database logic
        return true/false;
    }

    // All 13 interface methods overridden with specific implementations
}
```

**Example: StockServiceImpl**
```java
// StockServiceImpl.java (Line 238)
public class StockServiceImpl implements StockService {

    @Override
    public boolean recordStockInflow(int productId, int quantityReceived,
                                     int supplierId, String date) {
        // Specific implementation for stock inflow
        // Updates database, handles transactions
        notifyStockObservers();  // Triggers notifications
        return true;
    }

    @Override
    public void notifyStockObservers() {
        for (Runnable r : stockObservers) {
            r.run();  // Each observer can have different implementation
        }
    }
}
```

**Usage in DashboardPanel**:
```java
// DashboardPanel.java (Line 18)
private final ProductService productService = new ProductServiceImpl();

// Later used polymorphically
List<String> categories = productService.loadAllCategoryNames();
// Don't care about implementation, just call the interface method
```

**Method Overriding Examples**:
- LoginServiceImpl.validateCredentials() - Overrides interface method
- ProductServiceImpl.searchProducts() - Overrides interface method
- StockServiceImpl.loadLowStockItems() - Overrides interface method
- UserServiceImpl.addUser() - Overrides interface method

### Benefits
- **Flexibility**: Easy to switch between different implementations
- **Testability**: Can inject mock implementations for testing
- **Extensibility**: Add new implementations without changing existing code
- **Decoupling**: UI depends on interfaces, not concrete implementations
