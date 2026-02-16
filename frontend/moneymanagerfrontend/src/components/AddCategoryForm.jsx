import {useEffect, useState} from "react";
import {LoaderCircle} from "lucide-react";
import EmojiPickerPopup from "./EmojiPickerPopup.jsx";
import Input from "./Input.jsx";
const AddCategoryForm = ({onAddCategory,isEditing,initialCategoryData}) => {
    const [category, setCategory] = useState({
        categoryName: "",
        type: "income",
        iconUrl: ""
    })
    const [loading, setLoading] = useState(false);
    const categoryTypeOptions = [
        {value: "income", label: "Income"},
        {value: "expense", label: "Expense"},
    ]
    const handleChange = (key, value) => {
        setCategory({...category, [key]: value})
    }
    const handleSubmit = async () => {
        setLoading(true);
        try {
            await onAddCategory(category);
        }finally {
            setLoading(false);
        }
    }
    useEffect(() => {
        if (isEditing && initialCategoryData) {
            setCategory(initialCategoryData);
        } else {
            setCategory({categoryName: "", type: "income", iconUrl: ""});
        }
    }, [isEditing, initialCategoryData]);
  return (
    <div className="p-4">

         <EmojiPickerPopup
                iconUrl={category.iconUrl}
                onSelect={(selectedIcon) => handleChange("iconUrl", selectedIcon)}
            />

      <Input
                value={category.categoryName}
                onChange={({target}) => handleChange("categoryName", target.value)}
                label="Category Name"
                placeholder="e.g., Freelance, Salary, Groceries"
                type="text"
            />

            <Input
                label="Category Type"
                value={category.type}
                onChange={({target}) => handleChange("type", target.value)}
                isSelect={true}
                options={categoryTypeOptions}
            />
             <div className="flex justify-end mt-6">
                <button
                    type="button"
                    onClick={handleSubmit}
                    disabled={loading}
                    className="add-btn add-btn-fill">
                    {loading ? (
                        <>
                            <LoaderCircle className="w-4 h-4 animate-spin"/>
                            {isEditing ? "Updating..." : "Adding..."}
                        </>
                    ): (
                        <>
                            {isEditing ? "Update Category" : "Add Category"}
                        </>
                    )}
                </button>
            </div>
    </div>
  )
}

export default AddCategoryForm
