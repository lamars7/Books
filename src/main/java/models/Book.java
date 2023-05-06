package models;


public class Book {
    private int id;
    private String title;
    private double price;
    private String author;
    private int year;
    private String description;
    private String image;
	private String genre;

    
    public Book(int id, String title,double price,String author,int year,String description, String image, String genre) {
    	this.id = id;
    	this.title = title;
    	this.price = price;
    	this.author = author;
    	this.year = year;
    	this.description = description;
    	this.image = image;
    	this.genre = genre;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
}

