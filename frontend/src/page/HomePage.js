import Header from "../components/Header";
import NewPost from "../components/NewPost"

function HomePage() {

    const Body = () => {
        return (
            <div className="grid grid-cols-4">
                <div className="col-span-1">
                    <Left/>
                </div>
                <div className="col-span-2">
                    <Middle/>
                </div>
                <div className="col-span-1">
                    <Right/>
                </div>
            </div>
        );
    }

    const Left = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">Left</div>
        );
    }

    const Middle = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">
                <NewPost />
            </div>
        );
    }

    const Right = () => {
        return (
            <div className="bg-slate-100 m-6 p-6">Right</div>
        );
    }

    return (
        <div>
            <Header/>
            <Body/>
        </div>
    );
}

export default HomePage;