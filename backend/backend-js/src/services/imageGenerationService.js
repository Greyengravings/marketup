/**
 * ImageGenerationService
 * Generates high quality social media creative image URLs matching post context.
 */

export class ImageGenerationService {
  generate(data) {
    const heading = data.heading || this.generateHeading(data);
    const subheading = data.subheading || this.generateSubheading(data);
    const topic = `${heading} ${data.postType || ''} ${data.sector || ''} ${data.occasion || ''}`.toLowerCase();

    let imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80";

    if (topic.includes("festival") || topic.includes("diwali") || topic.includes("celebration")) {
      imageUrl = "https://images.unsplash.com/photo-1605807646983-377bc5a76493?w=800&auto=format&fit=crop&q=80";
    } else if (topic.includes("food") || topic.includes("chai") || topic.includes("restaurant") || topic.includes("bakery") || topic.includes("masala") || topic.includes("tea")) {
      imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop&q=80";
    } else if (topic.includes("fashion") || topic.includes("clothing") || topic.includes("sale") || topic.includes("store")) {
      imageUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=800&auto=format&fit=crop&q=80";
    } else if (topic.includes("gym") || topic.includes("fitness") || topic.includes("workout")) {
      imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=800&auto=format&fit=crop&q=80";
    } else if (topic.includes("salon") || topic.includes("beauty") || topic.includes("hair")) {
      imageUrl = "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=800&auto=format&fit=crop&q=80";
    } else if (topic.includes("real estate") || topic.includes("home") || topic.includes("property")) {
      imageUrl = "https://images.unsplash.com/photo-1560518883-ce09059eeffa?w=800&auto=format&fit=crop&q=80";
    }

    return {
      svg: '',
      imageUrl,
      copy: {
        heading,
        subheading,
        cta: data.cta || 'Learn More'
      }
    };
  }

  generateHeading(data) {
    const categories = {
      'Restaurant': ['Taste the Magic', 'Deliciously Yours', 'Fresh & Flavorsome'],
      'Gym': ['Push Your Limits', 'Get Fit Now', 'Stronger Everyday'],
      'Real Estate': ['Find Your Home', 'Luxury Living', 'Smart Investment'],
      'Generic': ['Quality You Trust', 'Elevate Your Life', 'Leading Excellence']
    };
    const list = categories[data.sector] || categories['Generic'];
    return list[Math.floor(Math.random() * list.length)];
  }

  generateSubheading(data) {
    return `Premium ${data.subSector || data.sector || 'services'} in ${data.city || 'your city'}.`;
  }
}

export const imageGenerationService = new ImageGenerationService();
