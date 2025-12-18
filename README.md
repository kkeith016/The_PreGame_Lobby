# PreGame Lobby - E-Commerce API

## Capstone Overview
PreGame Lobby is a video game-themed e-commerce app that I built as my final capstone project for the Java Development course. I used Spring Boot for the backend API and MySQL for the database, creating a fully functional online store with products, categories, and a shopping cart.

The main goal of this project was to step into the role of a backend developer for an existing website, adding new features and improvements while tackling real-world development challenges.

---

- **Category Management**  
  - CRUD operations for product categories.
  - Mapping database rows to Java objects using `mapRow()` methods.  

- **Product Management**  
  - CRUD operations for products.  
  - Search functionality by category, price, and subcategory.  

  - **Fixing Minor Front End Bug**
  - Such as a minor path disruption so images can load. 


## Lessons Learned
- **Database Management:** Automating database setup with `TestDatabaseConfig` simplified testing and development.  
- **DAO Design:** Centralized mapping (`mapRow`) and careful query construction reduced repetitive code and bugs.  
- **Error Handling:** Adding proper exception handling prevented runtime crashes and helped identify logic errors.  
- **BigDecimal Calculations:** Accurate financial calculations require careful use of BigDecimal arithmetic.
- **Check the Syntex and if your not sure check it again** its always lurking. 

## Future Improvements
If I were to continue this project, I would add:  

- **Guest Shopping Cart**  
  - Allow users to add items without logging in.  
  - Use a temporary `guestCartId` stored on the front-end and synced with the API on login.
  - much like a big name ecommerce store does


- **Product Reviews**  
  - Enable users to leave ratings and comments.  
  - Implement CRUD endpoints for reviews and display them using HTML/CSS.

- **Front-End Enhancements**  
  - Use **Bootstrap** for responsive design and layout.  
  - Dynamically fetch API data to display products, cart contents
  - Buttons more filter options
  - A dark mode because it is white and it is loud. 
